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


sealed trait Role

object Role {

  case object Administrator extends Role
  case object NormalUser extends Role
  case object Unconfirmed extends Role

  def valueOf(value: String): Role = value match {
    case "Administrator" => Administrator
    case "NormalUser"    => NormalUser
    case "Unconfirmed"   => Unconfirmed
    case _ => throw new IllegalArgumentException()
  }

}

case class DbUser(id: Int, email: String, username: String, role: Role,
                  password: String, name: String, address: String, 
                  confirmation_code: Option[String], phone: Option[String] ) {

}

object DbUser extends DbCommon[DbUser] {
  
  // -- Parsers
  
  
  override val simple = {
    get[Int]("user.id") ~
    get[String]("user.username") ~
    get[String]("user.address") ~
    get[String]("user.name") ~
    get[String]("user.email") ~
    get[String]("user.role") ~
    get[String]("user.password") ~
    get[Option[String]]("user.phone") ~
    get[Option[String]]("user.confirmation_code") map {
      case id~username~address~name~email~role~password~phone~confirmationcode => DbUser(id, email, username, Role.valueOf(role), password, name, address, confirmationcode, phone )
    }
  }
  
  val table = "user"
  
  def quickEdit(tmp: DbUser)(email: String, password: String): Option[DbUser] = {
       DB.withConnection { implicit connection =>
           SQL("update User set password=PASSWORD({password}),email={email} WHERE id={id}").
                on('email -> email,'password -> password, 
                   'id -> tmp.id ).executeUpdate()
           findById(tmp.id)
       }    
  }   
  /*
  def findByIdParse(id: Long): Option[DbUser] = {
    DB.withConnection { implicit connection =>
       findById(id).as(simple.singleOpt)
    }
  }
  */
  
  def confirmUser(code: String) = {
    DB.withConnection { implicit connection =>
      SQL("update user set role='NormalUser' where confirmation_code = {code} and role={role}").on('code -> code, 'role -> "Unconfirmed").executeUpdate()
    }    
  }
  
  
  def authenticate( email: String, password: String): Option[DbUser] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email} and password=PASSWORD({password})").on('email -> email,
          'password -> password).as(DbUser.simple.singleOpt)
    }
  }
  
  def checkFieldsUnique (values: List[(String,String)] ): Option[String] = {
    val errorsList = (for {
      (field,value) <- values
    } yield {  
        println(field)
        DB.withConnection { implicit connection =>
            SQL("select * from user where "+field+" = {value}").on('value-> value)
            .as(DbUser.simple.singleOpt)
            .flatMap { user => Some(field + " '" + value + "' already exists") }
        }
    }) 
    errorsList.flatten match {
      case Nil => None
      case _ => Some(errorsList.mkString("\n"))
    }
  }
  
  def resetPassword( email: String ) = {
    DB.withConnection { implicit connection =>
      println(email)
      val user = SQL("select * from user where email = {email}").on('email -> email).as(DbUser.simple.singleOpt)
      user map {
        user => val confirmation_code = Random.alphanumeric.take(32).mkString
                SQL("update user set confirmation_code={confirmation_code} where user_id={user_id}")
                .on('confirmation_code -> confirmation_code,
                    'user_id -> user.id)
                val new_user = user.copy( confirmation_code = Some(confirmation_code) )
                Mailer.sendResetMail(email, new_user)
        
      }
    }
  }
  
  def updateUser ( user: DbUser) = {
    DB.withConnection { implicit connection =>
      Some(SQL("update "+table+" set name={name}, address={address}, phone={phone}, password=PASSWORD({password}) where id={id}").
      on('name -> user.name, 
         'address -> user.address,
         'phone -> user.phone,
         'password -> user.password,
         'id -> user.id ).
         executeUpdate())  
    }        
  }
  
  def register ( email: String, 
                 name: String, 
                 password: String 
                  ): Option[DbUser] = {
        this.checkFieldsUnique ( List(("email",email),("username",email)) ).fold(
            DB.withConnection { implicit connection =>
                SQL("insert into user ( email, username, password, name, address, role, confirmation_code) " +
                "values({email}, {email},PASSWORD({password}),{name}, 'Not filled', 'Unconfirmed', {confirmation_code} )").
                on('email -> email, 'password -> password, 'name -> name,
                   'confirmation_code -> Random.alphanumeric.take(32).mkString ).
                executeInsert()
            .flatMap( userId => findById(userId) )
            }
            .flatMap ( dbUser => Some(Mailer.sendRegistrationMail(dbUser)) )
        )(
            errorMsg => None
        )
  }
  
}

