package org.ant.rtrules.engine

import org.ant.rtrules.types.Rule
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Recupera la lista de reglas almacenadas para un topic en concreto
  */
object RTRulesCore{

  /**
    * Recupera la lista de reglas correspondientes a un topic concreto
    * @param topic el topic del que se quieren obtener las reglas
    * @param obs observador que obtendrá los registros
    * @return Lista de las reglas recuperadas de la base de datos
    */
  //def getRulesByTopic(topic: String, obs: RTRuleObserver): List[Rule] =  {
  def getRulesByTopic(host:String, port: String, db:String, topic: String, obs: RTRuleObserver): List[Rule] =  {


    val client: MongoClient = MongoClient("mongodb://" + host + ":" + port + "/" + db)
    val dataBase: MongoDatabase = client.getDatabase(db)
    val collection: MongoCollection[Document] = dataBase.getCollection("user_rules")

    //creamos la query
    val query = collection.find(Document("{\"topic\" : \"" + topic + "\"})"))

    //subscribimos el observer
    query.subscribe(obs)

    //esperamos finalizacion
    Await.ready(query.toFuture, 10 seconds)

    //cerramos la conexion
    client.close()

    //convertimos los json en formato string a clases Rule
    RTRuleConversor.listStringToListRule(obs.rtRuleList)
  }

  /**
    * Recupera la lista de reglas correspondientes a un topic concreto
    * @param obs observador que obtendrá los registros
    * @return Lista de las reglas recuperadas de la base de datos
    */
  //def getAllRules(obs: RTRuleObserver): List[Rule] =  {
  def getAllRules(host:String, port: String, db:String, obs: RTRuleObserver): List[Rule] =  {

    val client: MongoClient = MongoClient("mongodb://" + host + ":" + port + "/" + db)
    val dataBase: MongoDatabase = client.getDatabase(db)
    val collection: MongoCollection[Document] = dataBase.getCollection("user_rules")

    //creamos la query
    val query = collection.find(Document("{}"))

    //subscribimos el observer
    query.subscribe(obs)

    //esperamos finalizacion
    Await.ready(query.toFuture, 10 seconds)

    //cerramos la conexion
    client.close()

    //convertimos los json en formato string a clases Rule
    RTRuleConversor.listStringToListRule(obs.rtRuleList)
  }

  /**
    * Obtiene de la base de datos las reglas asociadas a un topic
    * @param host ip de la instancia de mongodb
    * @param port puerto de la instancia de mongodb
    * @param db base de datos donde seencuentran las reglas
    * @param topic topic de las reglas a recuperar
    * @return
    */
  def getRules(host:String, port: String, db:String, topic: String): List[Rule] = {
    val observer = RTRuleObserver()
    getRulesByTopic(host, port, db, topic, observer) //obtenemos las reglas para el topic actual
  }


}
