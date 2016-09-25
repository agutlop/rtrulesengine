package org.ant.rtrules.types

/**
  * Define los métodos necesarios para que un objeto pueda ser evaluado por el motor de reglas
  */
trait Evaluable {

  /**
    * Devuelve el valor del campo nombrado como field
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  def getStringField(field:String):Option[String]

  /**
    * Devuelve el valor del campo nombrado como field
    * @param field El nombre del campo a obtener
    * @return Int con el contenido del campo, si éste existe
    */
  def getIntegerField(field:String):Option[Int]

  /**
    * Devuelve el valor del campo nombrado como field
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  def getLongField(field:String): Option[Long]

  /**
    * Devuelve el valor del campo nombrado como field
    * @param field El nombre del campo a obtener
    * @return Double con el contenido del campo, si éste existe
    */
  def getDoubleField(field:String): Option[Double]

  /**
    * Devuelve el valor del campo nombrado como field
    *
    * Las fechas siempre se almacenaran en formato EPOCH
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  def getDateField(field:String): Option[Long]

  /**
    * Devuelve el valor del campo nombrado como field
    *
    *
    * @param field El nombre del campo a obtener
    * @return Long con el contenido del campo, si éste existe
    */
  def getBooleanField(field:String): Option[Boolean]


  /**
    * Devuelve un campo de cualquier tipo en String
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  def getFieldLikeString(field:String): Option[String]

  /**
    * Devuelve un campo de cualquier tipo
    * @param field El nombre del campo a obtener
    * @return String con el contenido del campo, si éste existe
    */
  def getField(field:String): Option[Any]

}
