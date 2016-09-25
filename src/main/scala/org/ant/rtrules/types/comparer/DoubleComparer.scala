package org.ant.rtrules.types.comparer

/**
  * Comparador de datos de tipò Double
  */
object DoubleComparer {

  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left El Double sobre el que comparar
    * @param right El Double con el que comparar
    * @return true: si se cumple la condicion, false en otro caso
    */
  def compare(operador:String, left:Double, right:Double):Boolean = {
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
