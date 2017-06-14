package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps
import scala.util.Try
import scala.util.Random

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.routes
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import play.api.cache.Cache
import Math._


case class DbTransaction(id: Option[Int], user_id: Int, mnt_number: String, firstname: String, 
                  middlename: String, lastname: String, amount: Double, commission: Double, rate: Double, address: Option[String],
                  sent_on: Option[DateTime], status: Option[Int], created_at: Option[DateTime] ) {
    def fullName():String = {
      firstname + " " + middlename + " " + lastname
    }
    
    def transactionMinusFee(): Double = {
      Math.round((amount - commission)*rate*100.0)/100.0
    }
}

object DbTransaction extends DbCommon[DbTransaction] {
  
  override val table = "transactions"
  override val simple = {
    get[Int]("transactions.id") ~
    get[Int]("transactions.user_id") ~
    get[String]("transactions.mnt_number") ~
    get[String]("transactions.firstname") ~
    get[String]("transactions.middlename") ~
    get[String]("transactions.lastname") ~
    get[Double]("transactions.amount") ~
    get[Double]("transactions.commission") ~
    get[Double]("transactions.rate") ~
    get[Option[String]]("transactions.address") ~
    get[Option[DateTime]]("transactions.sent_on") ~
    get[Int]("transactions.status") ~
    get[DateTime]("transactions.created_at") map {
      case id~user_id~mnt_number~firstname~middlename~lastname~amount~commission~rate~address~sent_on~status~created_at 
          => DbTransaction(Some(id), user_id, mnt_number, firstname, middlename, lastname, amount, commission, rate, address, sent_on, Some(status), Some(created_at) )
    }
  }    
  
  def getRate(): Double = {
      val source_code = "btce"
      val asset = "btc"
      val currency = "usd"
      val rate_type = "high"
      // value is put into the cache by the actor
      Cache.getAs[Double]( source_code + "_" + asset + "_" + currency + "_" + rate_type ) getOrElse {          
        DbRate.getRate(source_code, asset, currency, rate_type) map {
          _.value
        } getOrElse 9999
      }
  }
  
  def listUsersTransactions ( page: Int, pageSize: Int, user_id: Int ): Page[DbTransaction] = {
    DB.withConnection { implicit connection =>
       val (totalRows,tmp) = listUnparsed ( page, pageSize, "", 1, Some("  user_id="+user_id.toString()) )
       val values = tmp.as(simple *)
       
       Page(values, page, page*pageSize, totalRows)    
    }
  }
  
  def addTransaction ( transaction: DbTransaction ): Option[DbTransaction] = {
    DB.withConnection { implicit connection =>
      SQL("insert into "+table+" ( user_id, mnt_number, firstname, middlename, lastname, "+
                               "amount, rate, sent_on, status, commission ) " +
      "values({user_id}, {mnt_number},{firstname},{middlename}, {lastname}, {amount}, {rate}, {sent_on}, {status}, {commission} )").
      on('user_id -> transaction.user_id,'mnt_number -> transaction.mnt_number, 
         'firstname -> transaction.firstname, 'middlename -> transaction.middlename,
         'lastname -> transaction.lastname, 'amount -> transaction.amount, 'rate -> transaction.rate, 'sent_on -> None,
         'status -> 0, 'commission -> transaction.commission ).
      executeInsert().flatMap { transId => findById(transId) }      
    }
  }
  
  def findByIdAndUser(id: Int, user_id: Int): Option[DbTransaction] = {
    DB.withConnection { implicit connection =>
      SQL("select * from "+table+" where user_id={user_id} and id = {id}").on('id -> id,
                                                                              'user_id -> user_id).as(simple.singleOpt)
    }
  }
  
  def updateAddress(transaction_id: Int, address: String) = {
    DB.withConnection { implicit connection =>
      Some(SQL("update "+table+" set address={address} where id={transaction_id}").
      on('address -> address, 
         'transaction_id -> transaction_id).
      executeUpdate())  
    }    
  }
  
  def paymentReceived ( transaction_id: Int) = {
    DB.withConnection { implicit connection =>
      Some(SQL("update "+table+" set status=1 where id={transaction_id}").
      on('transaction_id -> transaction_id).
      executeUpdate())  
    }        
  }
  
  def paymentProcessed ( transaction_id: Int) = {
    DB.withConnection { implicit connection =>
      Some(SQL("update "+table+" set status=2 where id={transaction_id}").
      on('transaction_id -> transaction_id).
      executeUpdate())  
    }        
  }
}