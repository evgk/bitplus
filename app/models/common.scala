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

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}


trait DbCommon[T] {
  
  val table: String
  val simple: RowParser[T];
  
  def findById(id: Long): Option[T] = {
    DB.withConnection { implicit connection =>
      SQL("select * from "+table+" where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }
  
  def deleteById(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from "+table+" where id = {id}").on('id -> id).executeUpdate()
    }
  }
  
  def genSelect(value: List[(String,String)], id: String, name: String, cls: String, defaultValue: Option[String] = None ): String = {
    val optionsList = for { 
      (key,label) <- value
    } yield {        
        val _value = defaultValue getOrElse ""
        val selected = if ( key == _value ) " selected=selected " else ""
        "<option "+selected+" value="+key+">"+label+"</option>"
    } 
    return "<select id='"+id+"' name='"+name+"' class='"+cls+"' size='1'>" +
           "<option value=''>Please select</option>"+
           optionsList.mkString("")+
           "</select>"
  }
  
  def listUnparsed(page: Int = 0, pageSize: Int = 10, joins: String, orderBy: Int = 1, filter: Option[String] ): (Long, SimpleSql[Row]) = {
    
    val offset = pageSize * page
    
    DB.withConnection { implicit connection =>
      
      val where = filter.getOrElse(" 1=1 ")
      
      println ( """
          select * from """+table+""" """ + joins + """
          where """+where+"""
          order by {orderBy} 
          limit {pageSize} offset {offset}
        """)
      val values = SQL(
        """
          select * from """+table+""" """ + joins + """
          where """+where+"""
          order by {orderBy} 
          limit {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize, 
        'offset -> offset,
        'orderBy -> orderBy
      )

      val totalRows = SQL(
        """
          select count(*) from """+table+""" """ + joins + """ 
            where """+where+"""
        """
      ).as(scalar[Long].single)
      
      (totalRows,values)
    }
  }  
  
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: Option[String] ): Page[T] = {
    
    val offset = pageSize * page
    val where = filter.getOrElse(" 1=1 ")
    
    DB.withConnection { implicit connection =>
      
      val values = SQL(
        """
          select * from """+table+""" t
          where """+where+"""
          order by {orderBy} 
          limit {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize, 
        'offset -> offset,
        'orderBy -> orderBy
      ).as(this.simple.*)

      val totalRows = SQL(
        """
          select count(*) from """+table+"""
          where """+where+"""
        """
      ).as(scalar[Long].single)

      Page(values, page, offset, totalRows)
      
    }
    
  }
  
  
}
