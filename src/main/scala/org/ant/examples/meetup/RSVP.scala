package org.ant.examples.meetup

import play.api.libs.json.Json


/**
  * Clases necesarias para almacenar un RSVP de meetup.
  * Se introducen tambien los implicitos para su parseo a JSON
  * */
case class MeetupEvent(
                        event_id: String,
                        event_name: Option[String],
                        event_url: Option[String],
                        time: Option[Long]
                      )

case class MeetupGroupTopics(
                              topic_name: Option[String],
                              urlkey: Option[String]
                            )

case class MeetupGroup(
                        group_id: Long,
                        group_name: String,
                        group_city: Option[String],
                        group_country: Option[String],
                        group_state: Option[String],
                        group_urlname: Option[String],
                        group_lat: Option[Double],
                        group_lon: Option[Double],
                        group_topics: List[MeetupGroupTopics]
                      )

case class MeetupMember(
                         member_id: Long,
                         member_name: Option[String],
                         //other_services: Option[String],
                         photo: Option[String]
                       )

case class MeetupVenue(
                        venue_id: Long,
                        venue_name: Option[String],
                        lat: Option[Double],
                        lon: Option[Double]
                      )

case class MeetupRsvp(
                       rsvp_id: Long,
                       response: String,
                       guests: Int,
                       mtime: Long,
                       visibility: String,
                       event: MeetupEvent,
                       group: MeetupGroup,
                       member: MeetupMember,
                       venue: Option[MeetupVenue]
                     )

/**
  * Implicitos para la conversion a JSON (lectura y escritura)
  */
object MeetupEvent {
  implicit val MeetupEventReads = Json.reads[MeetupEvent]
  implicit val MeetupEventWrites = Json.writes[MeetupEvent]
}

object MeetupGroupTopics {
  implicit val MeetupGroupTopicsReads = Json.reads[MeetupGroupTopics]
  implicit val MeetupGroupTopicsWrites = Json.writes[MeetupGroupTopics]
}

object MeetupGroup {
  implicit val MeetupGroupReads = Json.reads[MeetupGroup]
  implicit val MeetupGroupWrites = Json.writes[MeetupGroup]
}

object MeetupMember {
  implicit val MeetupMemberReads = Json.reads[MeetupMember]
  implicit val MeetupMemberWrites = Json.writes[MeetupMember]
}

object MeetupVenue {
  implicit val MeetupVenueReads = Json.reads[MeetupVenue]
  implicit val MeetupVenueWrites = Json.writes[MeetupVenue]
}

object MeetupRsvp {
  implicit val MeetupRsvpReads = Json.reads[MeetupRsvp]
  implicit val MeetupRsvpWrites= Json.writes[MeetupRsvp]
}