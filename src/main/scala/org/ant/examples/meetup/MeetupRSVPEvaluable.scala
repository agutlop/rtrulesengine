package org.ant.examples.meetup

import org.ant.rtrules.types.Evaluable
import play.api.libs.json.Json

/**
  * Crea la clase evaluable para un mensaje RSVP de Meetup
  */
case class MeetupRSVPEvaluable(rsvp_id: Long,
                          response: String,
                          guests: Int,
                          mtime: Long,
                          visibility: String,
                          event: MeetupEvent,
                          group: MeetupGroup,
                          member: MeetupMember,
                          venue: Option[MeetupVenue]) extends Evaluable  {


  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  override def getStringField(field: String): Option[String] = field match{
    case "response" => Some(response)
    case "visibility" => Some(visibility)
    case "event.event_id" => Some(event.event_id)
    case "event.event_name" => event.event_name
    case "event.event_url" => event.event_url
    case "group.group_name" => Some(group.group_name)
    case "group.group_city" => group.group_city
    case "group.group_country" => group.group_country
    case "group.group_urlname" => group.group_urlname
    case "group.group_topics.topic_name" => Some(group.group_topics.map(t => t.topic_name.getOrElse("")).filter( tm => tm!="").toString())
    case "group.group_topics.urlkey" => Some(group.group_topics.map(t => t.urlkey.getOrElse("")).filter( url => url!="").toString())
    case "member.member_name" => member.member_name
    //case "member.other_services" => member.other_services
    case "member.photo" => member.photo
    case "venue.venue_name" => venue match{
      case Some(MeetupVenue(id,name,lat,lon)) => venue.get.venue_name
      case None => None
    }
    case _ => None
  }

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Int con el contenido del campo, si éste existe
    */
override def getIntegerField(field: String): Option[Int] = None

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getLongField(field: String): Option[Long] = field match {
    case "group.group_id" => Some(group.group_id)
    case "member.member_id" => Some(member.member_id)
    case "venue.venue_id" => venue match{
      case Some(MeetupVenue(id,name,lat,lon)) => Some(venue.get.venue_id)
      case None => None
    }
    case _ => None
  }

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Double con el contenido del campo, si éste existe
    */
  override def getDoubleField(field: String): Option[Double] = field match {
    case "group.group_lat" => group.group_lat
    case "group.group_lon" => group.group_lon
    case "venue.lat" => venue match{
      case Some(MeetupVenue(id,name,lat,lon)) => venue.get.lat
      case None => None
    }
    case "venue.lon" => venue match{
      case Some(MeetupVenue(id,name,lat,lon)) => venue.get.lon
      case None => None
    }
    case _ => None
  }


  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getBooleanField(field: String): Option[Boolean] = None


  /**
    * Devuelve un campo de cualquier tipo en String
    *
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  override def getFieldLikeString(field: String): Option[String] = {
    getStringField(field) match {
      case Some(x:String) => Some(x)
      case None => getIntegerField(field) match {
        case Some(x:Int) => Some(x.toString)
        case None => getLongField(field) match {
          case Some(x:Long) => Some(x.toString)
          case None => getDoubleField(field) match {
            case Some(x:Double) => Some(x.toString)
            case None => getDateField(field) match {
              case Some(x:Long) => Some(x.toString)
              case None => None
            }
          }
        }
      }
    }
  }

  /**
    * Devuelve el campo
    * @param field
    * @return
    */
  override def getField(field:String): Option[Any] = {
    getStringField(field) match {
      case Some(x:String) => Some(x)
      case None => getIntegerField(field) match {
        case Some(x:Int) => Some(x)
        case None => getLongField(field) match {
          case Some(x:Long) => Some(x)
          case None => getDoubleField(field) match {
            case Some(x:Double) => Some(x)
            case None => getDateField(field) match {
              case Some(x:Long) => Some(x)
              case None => None
            }
          }
        }
      }
    }
  }


  /**
    * Devuelve el valor del campo nombrado como field
    *
    * Las fechas siempre se almacenaran en formato EPOCH (Long)
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getDateField(field: String): Option[Long] = field match {
    case "mtime" => Some(mtime)
    case "event.time" => event.time
    case _ => None
  }

}

//implicitos para la conversion a JSON
object MeetupRSVPEvaluable {
  implicit val MeetupRSVPEvaluableReads = Json.reads[MeetupRSVPEvaluable]
  implicit val MeetupRSVPEvaluableWrites= Json.writes[MeetupRSVPEvaluable]
}