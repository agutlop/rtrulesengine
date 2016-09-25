package org.ant.rtrules.types.comparer

/**
  * Comparador de datos de tipo Long
  */
object LongComparer {

  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left El Long sobre el que comparar
    * @param right El Long con el que comparar
    * @return true: si se cumple la condicion, false en otro caso
    * */
  def compare(operador:String, left:Long, right:Long):Boolean = {
    operador match {
      case "==" => left == right
      case ">=" => left >= right
      case "<=" => left <= right
      case ">" => left > right
      case "<" => left < right
      case "!=" => left != right
      case _ => false
    }
  }

}
