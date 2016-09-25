package org.ant.examples.words

import org.ant.rtrules.types.Evaluable

/**
  * Clase de ejemplo con solo un campo llamado "word"
  */
case class WordsEvaluable(val word:String) extends Evaluable{

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  override def getStringField(field: String): Option[String] = field match {
    case "word" => Some(word)
    case _ => None
  }

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getLongField(field: String): Option[Long] = None

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Int con el contenido del campo, si éste existe
    */
  override def getIntegerField(field: String): Option[Int] = None

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Double con el contenido del campo, si éste existe
    */
  override def getDoubleField(field: String): Option[Double] = None


  /**
    * Devuelve un campo de cualquier tipo en String
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  override def getFieldLikeString(field: String): Option[String] = field match {
    case "word" => Some(word)
    case _ => None
  }

  override def toString(): String = {
    "word" + ":" + word
  }

  /**
    * Devuelve un campo de cualquier tipo
    *
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  override def getField(field: String): Option[Any] = {
    getStringField(field) match {
      case Some(x:String) => Some(x)
      case None => getIntegerField(field) match {
        case Some(x:Int) => Some(x)
        case None => getLongField(field) match {
          case Some(x:Long) => Some(x)
          case None => getDoubleField(field) match {
            case Some(x:Double) => Some(x)
            case None => None
          }
        }
      }
    }
  }

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * Las fechas siempre se almacenaran en formato EPOCH (Long)
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getDateField(field: String): Option[Long] = None

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  override def getBooleanField(field: String): Option[Boolean] = None
}
