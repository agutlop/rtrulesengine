package org.ant.rtrules.engine

import org.ant.rtrules.types.Rule
import play.api.libs.json.Json

/**
  * Conversor de reglas a Json
  */
object RTRuleConversor {

  //obtiene una lista de Rules a partir de la lista de jsons en formato string
  def listStringToListRule(lstStr: List[String]): List[Rule] = {
    lstStr.map(str => stringToRule(str))
  }

  //convierte una cadena en formato Rule
  def stringToRule(str: String): Rule = {
    Json.parse(str).asOpt[Rule].get
  }

}