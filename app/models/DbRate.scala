package models

import java.util.{Date}
import java.util.Calendar

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

case class DbRate( source_code: String, asset: String, currency: String, 
                   value: Double, updated: Date, value_type: String )
                   
object DbRate extends DbCommon[DbRate] {
  
    val table = "rates"
    
    val simple = {
        get[String]("rates.source_code") ~
        get[String]("rates.asset") ~
        get[String]("rates.currency") ~
        get[Double]("rates.value") ~
        get[Date]("rates.updated") ~
        get[String]("rates.value_type") map {
            case source_code~asset~currency~value~updated~value_type=> 
              DbRate(source_code, asset, currency, value, updated, value_type  )
        }
    }
    
   def getRate(rate_source: String, asset: String, currency: String, rate_type: String ): Option[DbRate] = {
    DB.withConnection { implicit connection =>
      SQL("select * from "+this.table+" where source_code = {source_code} and " + 
          " asset = {asset} and currency = {currency} and value_type = {rate_type}").on('source_code -> rate_source,
              'asset -> asset,
              'currency -> currency,
              'rate_type -> rate_type).as(simple.singleOpt)
      }
    }
  
    def replaceRate ( source_code: String, asset: String,
                  currency: String, value: Double, value_type: String) = {
            DB.withConnection { implicit connection =>
                SQL("replace into "+table+" ( source_code, asset, currency, value, value_type, updated ) " +
                "values({source_code}, {asset}, {currency},{value}, {value_type}, {updated})").
                on('source_code -> source_code,'asset -> asset, 
                   'currency -> currency, 'value -> value,
                   'value_type -> value_type, 'updated-> Calendar.getInstance().getTime() ).
                executeInsert()
            }
    }
}