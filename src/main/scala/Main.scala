import akka.http.scaladsl.model.HttpResponse
import controller.RequestProcessor
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future

object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val port = 9000
  val interface = "0.0.0.0"

  val connections: Source[IncomingConnection, Future[ServerBinding]] = Tcp().bind( interface, port)

  connections.runForeach {
    c => c.handleWith( RequestProcessor.process(1000,System.getProperty("user.dir")))
  }

}
