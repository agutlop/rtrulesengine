package org.ant.rtrules.types

import play.api.libs.json.Json

/**
  * Representa las condiciones de una regla en forma de arbol.
  *
  * @param op Operador lógico a aplicar a las condicines
  * @param conns Lista de nodos hijos a evaluar
  * @param leaf Condicion a evaluar (para el caso de que el nodo sea de tipo LEAF)
  */
case class ConditionTree(op:String, conns: List[ConditionTree], leaf:Option[FieldCondition]) {

   //evalua el mensaje dependiendo del tipo de nodo en el que estemos
  def evaluate(msg:Evaluable):Boolean = {
      op match {
        case "AND" => conns.forall(_.evaluate(msg))//conns.foldLeft(true)((acc, curr) => acc && curr.evaluate(msg))
        case "OR" => conns.foldLeft(false)((acc, curr) => acc || curr.evaluate(msg))
        case "SINGLE" => conns.head.evaluate(msg)
        case "LEAF" => leaf.get.evaluate(msg)
        case _ => throw new Exception("ERROR - ConnectorJson.evaluate: Unknown operator -> " + op)
      }
   }
}

/**
  * Implicitos para la conversion a JSON (lectura y escritura)
  */
object ConditionTree {
  implicit val ConditionTreeReads = Json.reads[ConditionTree]
  implicit val ConditionTreeWrites = Json.writes[ConditionTree]
}