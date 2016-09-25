package org.ant.rtrules.types.comparer

/**
  * Comparador de datos de tipo Boolean
  */
object BooleanComparer extends {
  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left El Boolean al que comparar
    * @param right El Boolean con el que comparar
    * @return true: si se cumple la condicion, false en otro caso
    * */
  def compare(operador:String, left:Boolean, right:Boolean):Boolean = {

    operador match {
        case "==" => left == right
        case "!=" => left != right
        case _ => false
      }

    }
}
