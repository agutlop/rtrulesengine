package org.ant.rtrules.types

import org.ant.rtrules.types.comparer._
import play.api.libs.json.Json

/**
  * Validacion de la regla a nivel de campos
  *
  * @param operador tipo de comparacion == >= <= != < > ...
  * @param field nombre del campo a comparar
  * @param value valor contra el que comparar
  * @param tipo tipo de dato que se compara
  */
case class FieldCondition(operador:String, field:String, value:String, tipo:String){

  //evalua la condicion con los parámetros pasados
  def evaluate(msg: Evaluable): Boolean = {
    tipo match {

      case "String" =>
        msg.getStringField(field) match {
          case None => false //no existe el campo
          case Some(s) => StringComparer.compare(operador, s, value)
        }

      case "Integer" =>
        msg.getIntegerField(field) match {
          case None => false
          case Some(s) => IntegerComparer.compare(operador, s, value.toInt)
        }

      case "Long" =>
        msg.getLongField(field) match {
          case None => false
          case Some(s) => LongComparer.compare(operador, s, value.toLong)
        }

      case "Double" =>
        msg.getDoubleField(field) match {
          case None => false
          case Some(s) => DoubleComparer.compare(operador, s, value.toDouble)
        }
      case "Date" =>
        msg.getDateField(field) match {
          case None => false
          case Some(s) => DateComparer.compare(operador, s, value)
        }
      case "Boolean" =>
        msg.getBooleanField(field) match {
          case None => false
          case Some(s) => BooleanComparer.compare(operador, s, value.toBoolean)
        }
      case _ => throw new Exception("FieldCondition - Tipo no reconocido: " + tipo)
    }
  }
}

/**
  * Implicitos para la conversion a JSON (lectura y escritura)
  */
object FieldCondition{
  implicit val FieldConditionReads = Json.reads[FieldCondition]
  implicit val FieldConditionWrites = Json.writes[FieldCondition]
}