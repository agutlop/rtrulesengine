package org.ant.rtrules.types

import play.api.libs.json.Json

/**
  * Regla para aplicar
  *
  * @param _id Identificador de la regla
  * @param topic Topic de Kafa a la que va referida la regla
  * @param name Nombre de la regla
  * @param stage Punto donde aplica la regla
  * @param desc Descripcion de la regla
  * @param flag Indicador de Activado/desactivado
  * @param user Usuario que ha creado la regla
  * @param cond Condiciones a validar de la regla
  */
case class Rule(_id: RuleId,
                topic:String,
                name:String,
                stage:String,
                desc:String,
                flag:Boolean,
                user:String,
                cond:ConditionTree,
                action: String,
                actionFields: List[String]
                ){

  /**
    * Aplica la regla a un objeto que implementa Evaluable
    *
    * @param msg el mensaje al que aplicar la regla
    * @return true: si el mensaje cumple la regla. false: si no la cumple
    */
  def applyRule(msg: Evaluable): Boolean = {

    //if(this.isValid) cond.evaluate(msg)
    //else false
    cond.evaluate(msg)

  }


  /**
    * Aplica la regla a un objeto que implementa Evaluable
    *
    * @param msg el mensaje al que aplicar la regla
    * @return true: si el mensaje cumple la regla. false: si no la cumple
    */
  def applyRuleAndReturnMsg(msg: Evaluable): (Boolean, Map[String, Any]) = {

    if(applyRule(msg))
      (true, Map("time"->System.currentTimeMillis(),"rule_name" -> name, "result" -> buildResponseMsg(msg)))
    else
      (false, Map())

  }

  /**
    * Construye el mensaje de respuesta de la regla aplicada al mensaje actual
    * @param msg el mensaje del que obtener los campos
    * @return String con el mensaje a devolver
    */
  def buildResponseMsg(msg: Evaluable): String = action match{
    case "MESSAGE" => "ALERT: " + msg.toString()
    case "FIELDS" => actionFields.foldLeft("ALERT: ")((acc, curr) => acc + curr + ":" + msg.getFieldLikeString(curr).getOrElse("") + ", ")
    case "LITERALS" => actionFields.foldLeft("ALERT: ")((acc, curr) => acc + curr + ", ")
    case _ => ""
  }



}

//clase para poder recuperar el object id de mongodb
case class RuleId($oid: String)

/**
  * Implicitos para la conversion a JSON (lectura y escritura)
  */
object Rule{
  implicit val ruleIdReads = Json.reads[RuleId]
  implicit val ruleIdWrites = Json.writes[RuleId]
  implicit val JsonRuleReads = Json.reads[Rule]
  implicit val JsonRuleWrites = Json.writes[Rule]
}