package actors

import collection.mutable.HashMap
import play.api.mvc._
import play.api.Play.current
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.iteratee._
import scala.concurrent.{ExecutionContext, Future}
import jp.t2v.lab.play2.auth.AuthElement
import jp.t2v.lab.play2.auth.OptionalAuthElement
import jp.t2v.lab.play2.auth.LoginLogout
import views._
import models._
import actors._
import akka.actor._
import akka.actor.{Props, ActorRef, Actor}
import play.libs.Akka
import scala.concurrent.duration
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import scala.util.{Try,Success,Failure}
import play.core.actors._

import play.api.libs.json._

case class TransactionUpdate(transaction_id: Int,status: String)


object WebSockets {
  var requestIds: HashMap[String, String] = new HashMap[String, String]
  
  def sendUpdate(user_id: Int, transaction_id: Int, status: String) = {
    val requestId = requestIds.get(user_id.toString())
    println(requestIds)
    requestId map {
        requestId =>  
          println(requestId)
          implicit val timeout = Timeout(FiniteDuration(1, SECONDS))
          Akka.system.actorSelection( "/system/websockets/"+requestId.toString+"/handler" ).resolveOne().onComplete {
             case Success(actorRef) => println("Found actor")
                 actorRef ! TransactionUpdate(transaction_id, status) // logic with the actorRef
             case Failure(ex) => Logger.warn("/system/websockets/" + requestId.toString + "/handler" + " does not exist, error:" + ex.toString() )
                      
          }
    }
  }
  
  def props(out: ActorRef) = Props(new WebSocketsActor(out))
}


class WebSocketsActor(out: ActorRef) extends Actor {
  println(self.path)
  def receive = {
    case TransactionUpdate(transaction_id,status)  => {
            println("Sending message")
            out ! Json.toJson (Map("transaction_id" -> transaction_id.toString(), "status" -> status )).toString()
    }
      
  }
}


