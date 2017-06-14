package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
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


object Websockets extends Controller with AuthElement with LoginLogout with AuthConfigImpl {
  
  def socket = WebSocket.tryAcceptWithActor[String, String] { request =>
     Future.successful(request.session.get("websockid") match {
       case None => Left(Forbidden)
       case Some(websockid) =>
         println("putting " + request.id.toString() + " to " + websockid)
         WebSockets.requestIds.put(websockid, request.id.toString() )
         Right(WebSockets.props)
     })
  }

}