import kafka.serializer.StringDecoder
import org.apache.spark._
import org.apache.spark.streaming._
import org.ant.rtrules.engine.{RTRuleObserver, RTRulesCore}
import org.ant.rtrules.types.Rule
import org.ant.examples.words.WordsEvaluable
import org.apache.log4j.{BasicConfigurator, Level, Logger}
import org.apache.spark.streaming.kafka.KafkaUtils


object PruebaKafka extends App{
  val logger = BasicConfigurator.configure()
  val log = Logger.getLogger(getClass.getName)
  log.setLevel(Level.INFO)

  //solamente establecemos el nombre de la app
 // val conf = new SparkConf().setAppName("PruebaKafka")
  val conf = new SparkConf().setMaster("local[4]").setAppName("PruebaKafka")
  val ssc = new StreamingContext(conf, Seconds(10))

  val kafkaParams = Map[String, String]("metadata.broker.list" -> "node1:31000")
  val topicSet = Set("topic-prueba")

  val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)


  val observer = RTRuleObserver()

  //val core = RTRulesCore("192.168.33.1", "27017", "rtrules_db" )
  //var rules: List[Rule] = core.getDocsByTopic("topic-prueba", observer)
  var rules: List[Rule] = RTRulesCore.getAllRules("192.168.33.1", "27017", "rtrules_db",observer)
  ssc.sparkContext.parallelize(rules)

  //1. Parsear los mensajes a objetos del tipo Evaluable
  //lines.flatMap(str => str.split(" ")).map( _ => rules.foreach( rule => rule.applyRule(_))).print()
  lines.foreachRDD( println(_))

  //creamos los evaluables
  var evaluables = lines.flatMap(str => str._2.split(" ")).map( WordsEvaluable(_))


  evaluables.print()

  evaluables.foreachRDD( rdd => rdd.foreach( eval => println( rules.foreach( rule => println("Aplica Regla:\n " + rule + "\nAl mensaje\n " + eval + "\n Result: " + rule.applyRule(eval))) )))

  //2. Pasar a cada mensaje recibido cada una de las reglas recuperadas de base de datos
  //3. Realizar la accion asociada a la regla si es necesario





  ssc.start()             // Start the computation
  // ssc.awaitTermination()  // Wait for the computation to terminate
  // var i = 1
  while(true)
  {
    ssc.awaitTerminationOrTimeout(1000)
    //rules = core.getDocsByTopic("topic-prueba", observer)
    rules = RTRulesCore.getAllRules("192.168.33.1", "27017", "rtrules_db",observer)
    ssc.sparkContext.parallelize(rules)
  }



}
