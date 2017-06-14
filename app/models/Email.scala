package models

import play.api.libs.mailer._
import scala.util.Try

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
 * @author Evgeniy
 */
object Mailer {

  def sendMail(  header: String, to: String, message: Option[String], htmlMessage: Option[String] ) {
    
    val email = Email(
      header,
      "support@bitplus.so",
      Seq(to),
      // adds attachment
      /*
      attachments = Seq(
        AttachmentFile("attachment.pdf", new File("/some/path/attachment.pdf")),
        // adds inline attachment from byte array
        AttachmentData("data.txt", "data".getBytes, "text/plain", Some("Simple data"), Some(EmailAttachment.INLINE))
      ),*/
      // sends text, HTML or both...
      bodyText = message,
      bodyHtml = htmlMessage
    )
    MailerPlugin.send(email)
  }
  
  def getTransactionMail ( transaction: DbTransaction ) = {
    Some(views.html.mail.transaction ( transaction ).toString())    
  }
  
  def getResetPasswordMail ( user: DbUser ): Option[String] = {
    val baseUrl = Play.current.configuration.getString("base_url").get
    val link: String = baseUrl + routes.Application.reset(user.confirmation_code.get).url
    Some(views.html.mail.reset ( link, user.name ).toString())
  }
  
  def getRegistrationMail ( user: DbUser ): Option[String] = {
    val baseUrl = Play.current.configuration.getString("base_url").get
    val link: String = baseUrl + routes.Application.confirm(user.confirmation_code.get).url
    //println("Link: " + link)
    Some(views.html.mail.registration ( link, user.name ).toString())
  }

  def sendResetMail(email: String, dbUser: DbUser) = {
      sendMail( "Reset password request", 
                       email, 
                       getResetPasswordMail(dbUser),
                       getResetPasswordMail(dbUser))
  }
  
  def sendRegistrationMail(dbUser: DbUser): DbUser = {
      sendMail( "Welcome to Tradecoins", 
                       dbUser.email, 
                       getRegistrationMail(dbUser),
                       getRegistrationMail(dbUser))
      dbUser
  }

  def sendNotificationEmail(dbTransaction: DbTransaction)  = {
      sendMail( "Welcome to Tradecoins", 
                       "support@bitplus.so", 
                       getTransactionMail(dbTransaction),
                       getTransactionMail(dbTransaction))
  }
  
}