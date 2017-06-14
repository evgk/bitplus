package actors

import akka.actor.{Props, ActorRef, Actor}
import java.util.Random
import scala.collection.immutable.{HashSet, Queue}
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import play.libs.Akka
import models._
import play.api.Play.current
import play.api.libs.ws.WS
import play.api.cache.Cache


case object CheckTransactions

trait TransactionChecker {
  self: Actor =>
  val transTick = context.system.scheduler.schedule(5000.millis, 30000.millis, self, CheckTransactions)
}

class TransactionsCheckerActor extends Actor with TransactionChecker {

  def receive = {
    case CheckTransactions =>
      println("CheckTrans received")
      // get or create the actor for the rate and forward this message
        val transactions = DbTransaction.list(0, 100, 1, Some(" address is not null and status=0 ") )
        transactions.items map {
            case transaction => 
              val response = WS.url("https://blockchain.info/q/addressbalance/"+transaction.address.get+"?confirmations=2").get();
              response map {
                resp =>
                    val balance = resp.body.toDouble
                    println(transaction.amount)
                    println(balance)
                    if ( balance >= transaction.amount ) {
                      Mailer.sendNotificationEmail(transaction)
                      DbTransaction.paymentReceived(transaction.id.get) 
                      WebSockets.sendUpdate(transaction.user_id, transaction.id.get, "paid")
                    }
              }
        } 
        //println (transactions)
  }
}

object TransactionsCheckerActor {
  lazy val checkerActor: ActorRef = Akka.system.actorOf(Props(classOf[TransactionsCheckerActor])) 
  def start = {
    checkerActor ! CheckTransactions
  }
}
