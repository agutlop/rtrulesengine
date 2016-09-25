import org.ant.examples.meetup.{MeetupRSVPEvaluable, MeetupRsvp}
import org.apache.log4j.{BasicConfigurator, Logger}
import play.api.libs.json._


object Prueba extends App{
  val logger = BasicConfigurator.configure()
  val log = Logger.getLogger(getClass.getName)

  val source = scala.io.Source.fromFile("e:/example.txt")

  var mp = Map[String, Any]()

  for(line <- source.getLines())
  {
    val rsvp = Json.parse(line)//.asOpt[MeetupRsvp]//.get
    val rsvpEval = rsvp.as[MeetupRSVPEvaluable]
    println(rsvpEval)
    println(rsvpEval.getField("member.member_id").getOrElse(""))
    println(rsvpEval.getStringField("member.member_id").getOrElse(""))
    println(rsvpEval.getFieldLikeString("member.member_id").getOrElse(""))

    mp = mp+("member.member_id" -> rsvpEval.getField("member.member_id"))

  }

}
