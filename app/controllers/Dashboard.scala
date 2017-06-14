package controllers


import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.{ExecutionContext, Future}
import jp.t2v.lab.play2.auth.AuthElement
import jp.t2v.lab.play2.auth.LoginLogout


import anorm._

import views._
import models._
import actors._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

/**
 * @author Evgeniy
 */

object Dashboard extends Controller with AuthElement with LoginLogout with AuthConfigImpl {
   
  val transactionForm = Form {
    
    mapping("id" -> ignored[Option[Int]](None),
            "user_id" -> ignored[Int](0),    
            "mnt_number" ->  text, 
            "firstname" -> text,
            "middlename" -> text, 
            "lastname" -> text,
            "amount" -> of[Double],
            "commission"   -> ignored[Double](0),
            "rate"   -> ignored[Double](0),
            "address"   -> ignored[Option[String]](None),
            "sent_on" -> ignored[Option[DateTime]](None),
            "status" -> ignored[Option[Int]](None),
            "created_at" -> ignored[Option[DateTime]](None)
    )(DbTransaction.apply)(DbTransaction.unapply)
    .verifying("Some fields did not pass the validation", result => true )
  }
  
  val accountEditForm = Form {
    mapping("id" -> ignored[Int](0),            
            "email" -> ignored[String](""), 
            "username" -> ignored[String](""),
            "role" -> ignored[Role](Role.NormalUser),
            "password" -> text,
            "name" -> text, 
            "address" -> text,
            "confirmation_code" -> ignored[Option[String]](None),
            "phone" -> optional(text)
    )(DbUser.apply)(DbUser.unapply)
    
    
    .verifying("Some fields did not pass the validation", result => true )
  }  
  
  def transactions = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    val items = DbTransaction.listUsersTransactions (0, 200, this.loggedIn(request).id )
    Ok(views.html.user.transactions(items, loggedIn ))
  }
  
  def transactionsAdmin = StackAction (AuthorityKey -> Role.Administrator ) { implicit request =>
    val items = DbTransaction.list(0, 200, 1, Some(" status=1 ") ) 
    Ok(views.html.user.transactionsadmin(items, loggedIn ))
  }  
  
  def send = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    Ok(views.html.user.send(transactionForm, loggedIn))
  }
  
  def account = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    Ok(views.html.user.account(accountEditForm.fill(this.loggedIn(request)), loggedIn))
  }    
  
  def accountPost = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    accountEditForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors)
        BadRequest(views.html.user.account(formWithErrors, loggedIn)) 
      },
      formUser => {
        val user = formUser.copy (id = this.loggedIn(request).id,
                                  role = this.loggedIn(request).role,
                                  confirmation_code = this.loggedIn(request).confirmation_code)
        DbUser.updateUser(user)
        Redirect(routes.Dashboard.account)
      }
    )
  }
  
  def done(transactionId: Int) = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    val dbTransactionOpt = DbTransaction.findByIdAndUser(transactionId, this.loggedIn(request).id )
    //val tmp = dbTransactionOpt.getOrElse( Redirect(routes.Application.error) )
    (dbTransactionOpt map {
      case transaction => 
        if ( transaction.status.get == 1) Ok(views.html.user.done(transaction, loggedIn))
        else if ( transaction.status.get == 0) Redirect(routes.Dashboard.invoice(transaction.id.get))
        else if ( transaction.status.get == 2) Redirect(routes.Dashboard.invoicePaid(transaction.id.get))
        else Redirect(routes.Application.error)
    }).getOrElse( Redirect(routes.Application.error) )
  }  
  
  def invoice(transactionId: Int) = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    val dbTransactionOpt = DbTransaction.findByIdAndUser(transactionId, this.loggedIn(request).id )
    (dbTransactionOpt map {
      case transaction => 
        if ( transaction.status.get == 0) Ok(views.html.user.invoice(transaction, loggedIn))
        else if ( transaction.status.get == 1 ) Redirect(routes.Dashboard.done(transaction.id.get))
        else if ( transaction.status.get == 2) Redirect(routes.Dashboard.invoicePaid(transaction.id.get))
        else Redirect(routes.Application.error)
    }).getOrElse( Redirect(routes.Application.error) )
  }    
  
  def invoicePaid(transactionId: Int) = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    val dbTransactionOpt = DbTransaction.findByIdAndUser(transactionId, this.loggedIn(request).id )
    (dbTransactionOpt map {
      case transaction => 
        if ( transaction.status.get == 2) Ok(views.html.user.invoice_paid(transaction, loggedIn))
        else if ( transaction.status.get == 0 ) Redirect(routes.Dashboard.invoice(transaction.id.get))
        else if ( transaction.status.get == 1) Redirect(routes.Dashboard.done(transaction.id.get))
        else Redirect(routes.Application.error)
    }).getOrElse( Redirect(routes.Application.error) )
  }   
  
  
  def sendPost = StackAction (AuthorityKey -> Role.NormalUser) { implicit request =>
    transactionForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.user.send(formWithErrors, loggedIn)) 
      },
      transaction => {
        println("Amount:"+transaction.amount + ", commission: " + Play.current.configuration.getDouble("commission").get)
        val usertransaction = transaction.copy (user_id = this.loggedIn(request).id,
                                                rate = DbTransaction.getRate(),
                                                commission = transaction.amount * Play.current.configuration.getDouble("commission").get )
        val dbTransactionOpt = DbTransaction.addTransaction(usertransaction)
        
        (for { transaction <- dbTransactionOpt
              address <- DbAddress.getNewAddress(transaction.id.get)
              result <- DbTransaction.updateAddress(transaction.id.get, address.address)
        } yield Redirect(routes.Dashboard.invoice(transaction.id.get) )
        ).getOrElse (Redirect(routes.Application.addresserror))
      }
    )
  }  
  
  def process(transactionId: Int) = StackAction (AuthorityKey -> Role.Administrator) { implicit request =>
    val dbTransactionOpt = DbTransaction.findById(transactionId  )
    (dbTransactionOpt map {
      case transaction => 
        DbTransaction.paymentProcessed(transaction.id.get)
        WebSockets.sendUpdate(transaction.user_id, transaction.id.get, "processed")
        Redirect(routes.Dashboard.transactionsAdmin)        
    }).getOrElse( Redirect(routes.Application.error) )
  }  
  
}