package models

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


case class DbAddress( id: Int, address: String, transaction_id: Option[Int] )

object DbAddress extends DbCommon[DbAddress] {
    val table = "addresses"
    
    val simple = {
        get[Int]("addresses.id") ~
        get[String]("addresses.address") ~
        get[Int]("addresses.transaction_id")  map {
            case address_id~address~transaction_id=> 
              DbAddress( address_id, address, Some(transaction_id) )
        }
    }
    
    def findByTransactionId ( transaction_id: Int): Option[DbAddress] = {
      DB.withConnection { implicit connection =>
        SQL("select * from "+table+" where transaction_id = {transaction_id}").on('transaction_id -> transaction_id).as(simple.singleOpt)
      }
    }
    
    def getNewAddress ( transaction_id: Int): Option[DbAddress] = {
      DB.withConnection { implicit connection =>
                SQL("UPDATE "+table+" SET transaction_id={transaction_id} WHERE transaction_id is null limit 1").
                on('transaction_id->transaction_id.toString() ).executeUpdate()
                findByTransactionId(transaction_id)
      }
    }
}