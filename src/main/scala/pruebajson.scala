import org.ant.rtrules.engine.RTRuleConversor
import org.ant.rtrules.types._
import play.api.libs.json._



/**
  * Created by antuan on 5/07/16.
  */
object pruebajson extends App {
  val tree = ConditionTree("AND",
    List(
      ConditionTree(
        "AND",
        List(
          ConditionTree("LEAF",Nil,Some(FieldCondition("==","campo1","22","Integer"))),
          ConditionTree("LEAF",Nil,Some(FieldCondition("==","campo2","valor2","String")))
        ),
        None
      ),
      ConditionTree("LEAF",Nil,Some(FieldCondition("!=","campo3","valor3","String")))
    ),
    None)

  case class mensaje(campo1:Int, campo2:String, campo3:String) extends Evaluable {

    override def getStringField(field: String): Option[String] = field match{
      case "campo2" => Some(campo2)
      case "campo3" => Some(campo3)
      case _ => None
    }
    override def getLongField(field: String): Option[Long] = None
    override def getIntegerField(field: String): Option[Int] = field match{
      case "campo1" => Some(campo1)
      case _ => None
    }
    override def getDoubleField(field: String): Option[Double] = None

    override def getFieldLikeString(field: String): Option[String] = field match{
      case "campo1" => Some(campo1.toString)
      case "campo2" => Some(campo2)
      case "campo3" => Some(campo3)
      case _ => None
    }

    override def getField(field: String): Option[Any] = None

    override def getDateField(field: String): Option[Long] = None

    override def getBooleanField(field: String): Option[Boolean] = None
  }



  val jsonTree = Json.toJson(tree)
  println(Json.prettyPrint(jsonTree))

  val treeNew = jsonTree.asOpt[ConditionTree]

  assert(treeNew.get == tree)

  val rule = Rule(RuleId("1"),"topic-prueba","regla1", "stage1","regla de prueba 1", true,"yomismo",tree,"LITERALS",List("literal 1"))
  val rule2 = Rule(RuleId("2"),"topic-prueba","regla2","stage1","regla de prueba 2", true,"yomismo",tree,"MESSAGE",List())
  val rule3= Rule(RuleId("3"),"topic-prueba","regla3","stage1","regla de prueba 3", true,"yomismo",tree, "LITERALS",List("literal 3"))

  val ruleJson = Json.toJson(rule)
  println(Json.prettyPrint(ruleJson))
  val ruleNew = ruleJson.asOpt[Rule]

  assert(rule == ruleNew.get)



  println(ruleNew.get.applyRule(mensaje(22,"valor2","valor")))
  println(ruleNew.get.applyRule(mensaje(23,"valor2","valor3")))
  println(ruleNew.get.applyRule(mensaje(22,"valor","valor")))
  println(ruleNew.get.applyRule(mensaje(22,"valor2","valor3")))


 // val regla = "{ \"_id\" : \"1\", \"topic\" : \"topic-prueba\", \"desc\" : \"regla de prueba 1\", \"flag\" : true, \"user\" : \"yomismo\", \"cond\" : { \"op\" : \"AND\", \"conns\" : [{ \"op\" : \"AND\", \"conns\" : [{ \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"==\", \"field\" : \"campo1\", \"value\" : \"22\", \"tipo\" : \"Integer\" } }, { \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"==\", \"field\" : \"campo2\", \"value\" : \"valor2\", \"tipo\" : \"String\" } }] }, { \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"!=\", \"field\" : \"campo3\", \"value\" : \"valor3\", \"tipo\" : \"String\" } }] } }"

  val regla = "{ \"_id\" : \"1\", \"topic\" : \"topic-prueba\", \"stage\" : \"stage1\", \"desc\" : \"regla de prueba 1\", \"flag\" : true, \"user\" : \"yomismo\", \"cond\" : { \"op\" : \"AND\", \"conns\" : [{ \"op\" : \"AND\", \"conns\" : [{ \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"==\", \"field\" : \"campo1\", \"value\" : \"22\", \"tipo\" : \"Integer\" } }, { \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"==\", \"field\" : \"campo2\", \"value\" : \"valor2\", \"tipo\" : \"String\" } }] }, { \"op\" : \"LEAF\", \"conns\" : [], \"leaf\" : { \"operador\" : \"!=\", \"field\" : \"campo3\", \"value\" : \"valor3\", \"tipo\" : \"String\" } }] } }"
  val parseado = Json.parse(regla)
  println(parseado.toString())
  val reglaParseada =  Json.parse(regla).asOpt[Rule].get
  println(reglaParseada.applyRule(mensaje(22,"valor2","valor")))
  println(reglaParseada.applyRule(mensaje(23,"valor2","valor3")))
  println(reglaParseada.applyRule(mensaje(22,"valor","valor")))
  println(reglaParseada.applyRule(mensaje(22,"valor2","valor3")))

  val setRules = RTRuleConversor.listStringToListRule(List(regla, regla))

  setRules.foreach( x => println( x.applyRule(mensaje(22,"valor2","valor"))))

}