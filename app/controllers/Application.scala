package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.{ExecutionContext, Future}
import jp.t2v.lab.play2.auth.AuthElement
import jp.t2v.lab.play2.auth.OptionalAuthElement
import jp.t2v.lab.play2.auth.LoginLogout


import anorm._

import views._
import models._
import actors._

import play.api.libs.concurrent.Execution.Implicits.defaultContext


object Application extends Controller with OptionalAuthElement with LoginLogout with AuthConfigImpl {
  
  
  val registerForm = Form {
    mapping("email" -> nonEmptyText.verifying("Email already exists", userEmail => DbUser.checkFieldsUnique(List(("email", userEmail) )).isEmpty ), 
            "name" -> text, 
            "password" -> text
    )(DbUser.register)(x => (Some(( x.get.email, x.get.name, x.get.password )) ))
    .verifying("Some fields did not pass the validation", result => result.isDefined )
  }

  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(DbUser.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  val ratesFetcher = RatesActor.ratesActor
  RatesActor.start
  
  val transactionChecker = TransactionsCheckerActor.checkerActor
  TransactionsCheckerActor.start
  
  def error = Action {
    Ok(views.html.error())
  }
  
  def addresserror = Action {
    Ok(views.html.error())
  }  
  
  val Pattern = "(iPhone|webOS|iPod|Android|BlackBerry|mobile|SAMSUNG|IEMobile|OperaMobi)".r.unanchored

  def isMobile[A](implicit request: Request[A]): Boolean = {
    request.headers.get("User-Agent").exists(agent => {
      agent match {
        case Pattern(a) => true
        case _ => false
      }
    })
  }
  
  def index = StackAction { implicit request =>
    val maybeUser: Option[DbUser] = loggedIn
    Ok(views.html.index(registerForm, maybeUser, isMobile))
  }

  def login = Action {
    Ok(views.html.user.login(loginForm))
  }
  
  def forget = Action {
    Ok(views.html.user.forgot())
  }
  
  def forgetPost = Action(parse.tolerantFormUrlEncoded) { implicit request =>
    val email = request.body.get("email")
    email map {
     seq => seq map { 
      email =>
        DbUser.resetPassword(email)
      }
    }
    Ok(views.html.user.forgot())
  }  
  
  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }  
  
  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.user.login(formWithErrors))),
      user =>  gotoLoginSucceeded(user.get.id).flatMap( r => Future(r.withSession(("connected",user.get.name), ("websockid", user.get.id.toString() ) )) ) /*Future.successful(Redirect(routes.Application.index))*/
    )
  }  
  
  def register = StackAction { implicit request =>
    val maybeUser: Option[DbUser] = loggedIn
    registerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.index(formWithErrors, maybeUser, isMobile)),
      user =>  Redirect(routes.Application.index) /*Future.successful(Redirect(routes.Application.index))*/
    )
  } 
  
  def confirm(code: String) = Action.async { implicit request =>
    DbUser.confirmUser( code )
    Future.successful(Redirect(routes.Application.login))
  }

  def instructions() = StackAction { implicit request =>
    Ok(views.html.instructions())
  }
  
  def reset(code: String) = Action.async { implicit request =>
    Future.successful(Ok(views.html.user.reset()))
  }  
  
  def untrail(path: String) = Action { 
    MovedPermanently("/" + path)
  }

}
