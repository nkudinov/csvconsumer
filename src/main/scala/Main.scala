package org.nkudinov
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl._
import org.nkudinov.controller.RequestProcessor
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Try

object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val port = 9000
  val interface = "0.0.0.0"

  val connections: Source[IncomingConnection, Future[ServerBinding]] = Tcp().bind( interface, port)

  val batchSize = 1000

  val retVal = connections.runForeach {
    c => c.handleWith( RequestProcessor.process( batchSize,System.getProperty("user.dir")))
  }
  println("Please press Enter or Ctrl-c to stop service ...")
  Try(StdIn.readLine())
  system.terminate()
}
