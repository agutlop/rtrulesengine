package org.ant.rtrules.types.comparer

import java.text.SimpleDateFormat
import java.text.ParsePosition
import java.util.Date

/**
  * Comparador de datos de tipo Date. Formato dd/mm/yyyy hh:mm:ss
  */
object DateComparer {

  var sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");


  /**
    * Compara los campos left y right usando el comparador operador
    * @param operador El tipo de comparacion
    * @param left El Date sobre el que comparar (estará en formato EPOCH(Long))
    * @param right El Date con el que comparar (vendrá en formato String)
    * @return true: si se cumple la condicion, false en otro caso
    * */
  def compare(operador:String, left:Long, right:String):Boolean = {

    val dright = sdf.parse(right, new ParsePosition(0)).getTime

    operador match {
      case "==" => left == dright
      case ">=" => left >= dright
      case "<=" => left <= dright
      case ">" => left > dright
      case "<" => left < dright
      case "!=" => left != dright
      case _ => false
    }
  }

}
