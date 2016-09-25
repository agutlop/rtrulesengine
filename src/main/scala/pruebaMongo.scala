import org.ant.rtrules.engine.{RTRuleObserver, RTRulesCore}
import org.ant.rtrules.types.Rule
import org.apache.log4j.{BasicConfigurator, Logger}


object pruebaMongo extends App {

  val logger = BasicConfigurator.configure()
  val log = Logger.getLogger(getClass.getName)

  val observer = RTRuleObserver()
  //val core = RTRulesCore("localhost", "27017", "rtrules_db" )
  val rules: List[Rule] = RTRulesCore.getRulesByTopic("localhost", "27017", "rtrules_db" ,"topic prueba 1", observer)
  rules.foreach(println)

  val rules2: List[Rule] = RTRulesCore.getAllRules("localhost", "27017", "rtrules_db" ,observer)
  rules2.foreach(println)

}
