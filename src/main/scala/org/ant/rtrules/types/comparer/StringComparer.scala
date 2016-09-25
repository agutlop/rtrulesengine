package org.ant.rtrules.types.comparer

/**
  * Clase para la comparacion de datos de tipo String
  */
object StringComparer {

  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left La cadena sobre la que comparar
    * @param right La cadena con la que comparar
    * @return true: si se cumple la condicion, false en otro caso
    */
  def compare(operador:String, left:String, right:String):Boolean = {
    operador match {
      case "equals" => left == right
      case "not equals" => left != right
      case "contains" => left.contains(right)
      case "not contains" => left.contains(right)
      case "begins with" => left.indexOf(right)==0
      case "ends with" => left.indexOf(right) == left.length-right.length
      case _ => false
    }
  }
}
