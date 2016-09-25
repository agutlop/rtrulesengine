package org.ant.examples

import kafka.serializer.StringDecoder
import org.ant.examples.words._
import org.ant.rtrules.engine.RTRulesCore
import org.apache.log4j.{BasicConfigurator, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark._


/**
  * Programa de ejemplo: WordsCountAppES (Salida a Elastic Search)
  *
  * Las reglas se aplican a cada palabra recibida en los mensajes de kafka
  * El Topic para esta app es "topic-wordcountapp"
  *
  * args(0) - broker list de Kafka
  * args(1) - Ip de MongoDB
  * args(2) - Puerto de MongoDB
  * args(3) - Base de datos de las reglas
  *
  * Ejemplo:
  *   WordCountAppES node3:31000 192.168.33.1 27017 rtrules_db
  */
object WordCountAppES{

  final val STAGE1: String = "stage1"
  final val STAGE2: String = "stage2"

  def main(args: Array[String]) {

    val logger = BasicConfigurator.configure()
    val log = Logger.getLogger(getClass.getName)

    log.info("Iniciamos WordCountAppES:")
    log.info("    - metadata.broker.list - " + args(0))
    log.info("    - MongoDB ip           - " + args(1))
    log.info("    - MongoDB port         - " + args(2))
    log.info("    - DataBase name        - " + args(3))


    //cargamos la configuracion
    val conf = new SparkConf().setAppName("WordCountAppES")
      //.setMaster("local[4]")


    //elastic search props
    conf.set("es.index.auto.create", "true")
    conf.set("es.resource", "topic-wordcountapp/rule_name")
    conf.set("es.resource.write", "topic-wordcountapp/{rule_name}")
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
    val topicSet = Set("topic-wordcountapp")
    val kafkaMsg = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)


    //obtenemos las palabras de los mensajes
    val words = kafkaMsg.flatMap(msg => msg._2.split(" "))

    //Convertimos a WordsEvaluabes para poder aplicar las reglas del Stage1
    val wordsEval = words.map(WordsEvaluable(_))

    //aplicamos las reglas del Stage1 (palabras tal y como llegan)
    wordsEval.foreachRDD(
      rdd => {
        val partmapped = rdd.mapPartitions(
          rddPart => {
            //obtenemos las regals para esta stage
            //val rules = Rules.getRules(hostb.value, portb.value, dbb.value, "topic-wordapp").filter(rule => rule.stage == STAGE1)
            val rules = RTRulesCore.getRules(hostb.value, portb.value, dbb.value, "topic-wordcountapp").filter(rule => rule.stage == STAGE1)

            //aplicamos a cada WE las reglas obtenidas
            rddPart.flatMap(
              we => {
                rules.map(
                  rule => {
                    println("Applying rule STAGE1: " + rule + "   to WE: " + we)
                    rule.applyRuleAndReturnMsg(we)
                  }
                ).filter(res => res._1)//filtramos las que han dado true
              }
            )
          }
        )
        //enviamos a ES
        partmapped.map( reg => reg._2).saveToEs("topic-wordcountapp/{rule_name}")
      }
    )

    //realizamos el word Count
    //aplicamos transformaciones
    val wordsPair = words.map(word => (word, 1))
    val wordsCount = wordsPair.reduceByKey(_ + _)

    //Convertimos a WordCountEvaluable para poder aplicar las reglas
    val wordsCountEval = wordsCount.map(pair => WordCountEvaluable(pair._1, pair._2))

    //aplicamos las reglas del Stage1 (palabras tal y como llegan)
    wordsCountEval.foreachRDD(
      rdd => {
        val partmapped = rdd.mapPartitions(
          rddPart => {
            //obtenemos las regals para esta stage
            val rules = RTRulesCore.getRules(hostb.value, portb.value, dbb.value, "topic-wordcountapp").filter(rule => rule.stage == STAGE2)

            //aplicamos a cada WE las reglas obtenidas
            rddPart.flatMap(
              we => {
                rules.map(
                  rule => {
                    println("Applying rule STAGE2: " + rule + "   to WE: " + we)
                    rule.applyRuleAndReturnMsg(we)
                  }
                ).filter(res => res._1)//filtramos las que han dado true
              }
            )
          }
        )
        //enviamos a ES
        partmapped.map( reg => reg._2).saveToEs("topic-wordcountapp/{rule_name}")
      }
    )

    //Lanzamos la aplicacion
    ssc.start()
    ssc.awaitTermination()
  }
}
