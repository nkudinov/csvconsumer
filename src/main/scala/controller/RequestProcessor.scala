package controller

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.Instant

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Framing, Sink}
import akka.util.ByteString
import model.{Row, Stat}
object RequestProcessor {

  private def decode(line:String):Row ={
    val arr = line.split(",")
    Row(arr(0), arr(1), BigDecimal(arr(2)), BigInt(arr(3)), BigInt(arr(4)))
  }

  private def toStat(list:Seq[Row])={
    val sum5 = list.map(_.value3).sum
    val uniqUsersCount = list.map(_.uuid).distinct.size
    val agg = list.groupBy(_.uuid).map{case (uuid,agg) => (uuid,agg.map(_.value1).sum/agg.size, agg.last.value2)}.toList
    Stat(sum5,uniqUsersCount,agg)
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
    .map(_.toString)
    //.alsoTo( FileIO.toPath(Paths.get(directory,"???")))
    .alsoTo( Sink.foreach(str => writeToFile(directory, str)))
    .map(_ => ByteString())

}
