package org.ant.rtrules.types.comparer

/**
  * Comparador de tipos enteros
  */
object IntegerComparer {

  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left El entero sobre el que comparar
    * @param right El entero con el que comparar
    * @return true: si se cumple la condicion, false en otro caso
    */
  def compare(operador:String, left:Int, right:Int):Boolean = {
    operador match {
      case "==" => left == right
      case ">=" => left >= right
      case "<=" => left <= right
      case ">"  => left > right
      case "<"  => left < right
      case "!=" => left != right
      case _ => false
    }
  }
}
