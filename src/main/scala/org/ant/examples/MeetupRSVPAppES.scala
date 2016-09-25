package org.ant.examples

import kafka.serializer.StringDecoder
import org.ant.examples.meetup.MeetupRSVPEvaluable
import org.ant.rtrules.engine.RTRulesCore
import org.apache.log4j.{BasicConfigurator, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark._
import play.api.libs.json.Json


/**
  * Programa de ejemplo: MeetupRSVPAppES (Salida a Elastic Search)
  *
  * Las reglas se aplican los mensajes recibidos de meetup
  * El Topic para esta app es "topic-meetuprsvp"
  *
  * args(0) - broker list de Kafka
  * args(1) - Ip de MongoDB
  * args(2) - Puerto de MongoDB
  * args(3) - Base de datos de las reglas
  *
  * Ejemplo:
  *   WordCountAppES node3:31000 192.168.33.1 27017 rtrules_db
  */
object MeetupRSVPAppES{

  final val STAGE1: String = "stage1"
  final val STAGE2: String = "stage2"

  def main(args: Array[String]) {

    val logger = BasicConfigurator.configure()
    val log = Logger.getLogger(getClass.getName)

    log.info("Iniciamos MeetupRSVPAppES:")
    log.info("    - metadata.broker.list - " + args(0))
    log.info("    - MongoDB ip           - " + args(1))
    log.info("    - MongoDB port         - " + args(2))
    log.info("    - DataBase name        - " + args(3))


    //cargamos la configuracion
    val conf = new SparkConf().setAppName("MeetupRSVPAppES")
     // .setMaster("local[4]")


    //elastic search props
    conf.set("es.index.auto.create", "true")
    conf.set("es.resource", "topic-meetuprsvp/rule_name")
    conf.set("es.resource.write", "topic-meetuprsvp/{rule_name}")
    conf.set("es.nodes", "192.168.33.1:9200")
    conf.set("es.port", "9200")
    conf.set("es.nodes.wan.only","true")
    conf.set("es.nodes.discovery","false")

    //creamos el streaming context
    val ssc = new StreamingContext(conf, Seconds(5))

    //almacenamos los argumentos de entrada para las rules
    val host = args(1)
    val port = args(2)
    val db = args(3)
    val hostb = ssc.sparkContext.broadcast(host)
    val portb = ssc.sparkContext.broadcast(port)
    val dbb = ssc.sparkContext.broadcast(db)


    //creamos la conexion a Kafka con todos sus parámetros
    val kafkaParams = Map[String, String]("metadata.broker.list" -> args(0))
    val topicSet = Set("topic-meetuprsvp")
    val kafkaMsg = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)


    //obtenemos los rsvps
    val rsvp = kafkaMsg.map(msg => msg._2)
    rsvp.foreachRDD(_.foreach(println(_)))


    //convertimos a Json y luego a Evaluable
    val rsvpEval = rsvp.map( msg => Json.parse(msg).as[MeetupRSVPEvaluable])
    rsvpEval.foreachRDD(_.foreach(println(_)))

    //aplicamos las reglas del Stage1 (palabras tal y como llegan)
    rsvpEval.foreachRDD(
      rdd => {
        val partmapped = rdd.mapPartitions(
          rddPart => {

            //obtenemos las regals para esta stage
            val rules = RTRulesCore.getRules(hostb.value, portb.value, dbb.value, "topic-meetuprsvp").filter(rule => rule.stage == STAGE1)

            //aplicamos a cada rsvp las reglas obtenidas
            rddPart.flatMap(
              rsvp => {
                rules.map(
                  rule => {
                    println("Applying rule STAGE1: " + rule + "   to WE: " + rsvp)
                    rule.applyRuleAndReturnMsg(rsvp)
                  }
                ).filter(res => res._1)//filtramos las que han dado true
              }
            )
          }
        )
        //enviamos a ES
        partmapped.map( reg => reg._2).saveToEs("topic-meetuprsvp/{rule_name}")
      }
    )


    //Lanzamos la aplicacion
    ssc.start()
    ssc.awaitTermination()
  }
}
