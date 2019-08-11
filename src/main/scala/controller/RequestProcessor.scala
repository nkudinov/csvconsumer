package org.nkudinov.controller

import java.math.MathContext
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.Instant
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Framing, Sink}
import akka.util.ByteString
import org.nkudinov.model.{Row, Stat, UserStat}
import scala.collection.mutable

object RequestProcessor {
  private val mc = new MathContext(16)

  private[controller] def decode(line:String):Row ={
    val arr = line.split(",")
    Row(arr(0), arr(1), BigDecimal(arr(2)), BigInt(arr(3)), BigInt(arr(4)))
  }

  private[controller] def toStat(list:Seq[Row])={
    val sum5 = list.map(_.field5).sum
    val uniqueNumberOfUsers = list.map(_.uuid).distinct.size

    //  I can not use "standard" groupBy method because according to task i need to prevent Order of UUID!
    //  val usersStat = list.groupBy(_.uuid).map {
    //      case (uuid, agg) => {
    //        UserStat(uuid, (agg.map(_.filed3).sum / agg.size).round(mc), agg.last.filed4)
    //      }
    //  }.toSeq

    val map = mutable.LinkedHashMap[String, mutable.LinkedHashSet[(BigDecimal,BigInt)]]().withDefault(_ => mutable.LinkedHashSet[(BigDecimal,BigInt)]())
    for (e <- list) {
      val key =  e.uuid
      map(key) = map(key) + ((e.filed3,e.filed4))
    }


    Stat(sum5, uniqueNumberOfUsers, map.map{ case (uuid, set) => UserStat(uuid,(set.map(_._1).sum/set.size).round(mc),set.last._2) }.toList
    )

  }
  private def makeFileName(instant: Instant):String ={
    s"${instant.toEpochMilli()}.csv"
  }
  private def writeToFile(directory:String, lines:String):Unit = {
    Files.write(Paths.get(directory,makeFileName(Instant.now())), lines.getBytes(StandardCharsets.UTF_8))
  }

  def process(n:Int,directory:String)(implicit system:ActorSystem, materializer:ActorMaterializer) = Flow[ByteString]
    .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 512))
    .map(_.utf8String)
    .map(decode)
    .grouped(n)
    .map(toStat)
    .map(_.show)
    .alsoTo( Sink.foreach(str => writeToFile(directory, str)))
    .map(_ => ByteString())

}
