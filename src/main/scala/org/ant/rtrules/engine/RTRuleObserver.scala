package org.ant.rtrules.engine

import org.apache.log4j.Logger
import org.mongodb.scala.{Observer, Subscription}
import org.mongodb.scala.bson.collection.immutable.Document

/**
  * Esta clase será la encargada de recibir la consulta de las reglas realizada a mongodb
  */
case class RTRuleObserver() extends Observer[Document]{

  var rtRuleList: List[String] = List[String]()
  val log = Logger.getLogger(getClass.getName)

  override def onError(e: Throwable): Unit = { log.error( s"rtRuleObserver - There was an error: $e") }

  override def onSubscribe(subscription: Subscription): Unit = {
    log.debug("Suscrito correctamente a " + subscription.toString)
    rtRuleList = List[String]()
    subscription.request(10000)
  }

  override def onComplete(): Unit = {
    log.debug("Suscripcion completa. Se han obtenido " + rtRuleList.length + " registros")
  }

  override def onNext(doc: Document): Unit = {
    log.debug("Nuevo registro: " + doc.toJson().toString())
    rtRuleList = rtRuleList :+ doc.toJson().toString()
  }
}
