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


case object FetchLatest
case object StartFetchers

case class ImportRates(source_code: String, asset: String, currency: String, valuetype: String, value: Double)


trait RateFetcher {
  self: Actor =>
  val stockTick = context.system.scheduler.schedule(Duration.Zero, 60000.millis, self, FetchLatest)
  val rateSource: String
}

class Btce extends Actor with RateFetcher {
  val rateSource = "BTCE"
  val pairs = List(("BTC","USD"))
   def receive = {
    case FetchLatest =>
        pairs.map {
           case (asset, currency) => WS.url("https://btc-e.com/api/2/"+asset.toLowerCase() + 
              "_" + currency.toLowerCase() + "/ticker").get().map { response =>
                val mapping = List(("high","high"), ("low","low"), ("avg","avg"), ("buy", "bid"), ("sell","ask") )
                mapping map {
                    case (type_btce, type_local) => (type_btce, (response.json \ "ticker" \ type_btce).as[Double])
                } map {
                  case (valuetype, value) => RatesActor.ratesActor ! ImportRates(rateSource,asset,currency,valuetype,value)
                }
                                
           }

        }
        
    
   }  
}

class Bitstamp extends Actor with RateFetcher {
   val rateSource = "BITSTAMP"
   def receive = {
    case FetchLatest =>
      RatesActor.ratesActor ! ImportRates(rateSource,"test","test","test",0)
   }  
}

class Coinbase extends Actor with RateFetcher {
   val rateSource = "COINBASE"
   def receive = {
    case FetchLatest =>
      RatesActor.ratesActor ! ImportRates(rateSource,"test","test","test",0)
   }  
}

class RatesActor extends Actor {
  val Rates = List(("BTCE", Props(new Btce) ),
                   ("BITSTAMP", Props(new Bitstamp) ),
                   ("COINBASE", Props(new Coinbase) ))
                   
  def receive = {
    case StartFetchers =>
      // get or create the StockActor for the symbol and forward this message
      Rates.map {
        case (key, value) => context.child(key).getOrElse{  
          context.actorOf(value, key)
        }
      }
    case importRates @ ImportRates(source_code, asset, currency, valuetype, value)=>
      println(importRates)
      DbRate.replaceRate(source_code, asset, currency, value, valuetype )
      val cache_key = source_code + "_" + asset + "_" + currency + "_" + valuetype
      Cache.set(cache_key, value)
  }
}

object RatesActor {
  lazy val ratesActor: ActorRef = Akka.system.actorOf(Props(classOf[RatesActor])) 
  def start = {
    ratesActor ! StartFetchers
  }
}


