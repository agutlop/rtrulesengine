# RTRulesEngine
Aplicación Spark Streaming para aplicar reglas de negocio de usuario a un stream de datos para el proyecto fin de master del Experto en Big Data de la U-Tad Curso 2015-2016.

## Prerequisitos

Para compilar el proyecto es necesario tener las siguientes versiones de Scala y Sbt

* Scala 2.11.8
* Sbt 0.13.11

## Compilación

Para compilar el proyecto es necesario descargart el repositorio y una vez dentro de la carpeta del proyecto ejecutar sbt

`sbt compile assembly`

Este paso nos generará un jar con el que poder submitir jobs a un cluster preparado para Spark.

## Ejecucion

Este proyecto esta implementado para lanzar sus jobs de Spark Streaming sobre el cluster creado a partir del repositorio https://github.com/agutlop/rtrules-mesos-cluster

Los jobs de Spark Streaming necesitan tener levantadas y visibles las instancias de mongodb y elasticsearch en los puertos indicados en el repositorio https://github.com/agutlop/rtrules-storage

Es necesaria también la creacion de los topics de kafka de los jobs. 

Una vez compilado el proyecto será necesario copiar el jar generado a una ruta accesible por el cluster para poder submitirlo. En Vagrant, el directorio del proyecto esta montado en cada uno de los nodos del cluster en la ruta /vagrant, por lo que se puede copiar el jar a esta ruta y submitir el job desde ella

Este proyecto tiene los siguientes jobs implementados para submitir al cluster de la siguiente manera:
* topic-wordapp: 

  `spark-submit --class org.ant.examples.ESWordsApp --master mesos://192.168.33.10:7077 --deploy-mode cluster --driver-cores 0.5 --executor-memory 600M --total-executor-cores 7 spark-pruebas-assembly-1.0.jar node2:31000 192.168.33.1 27017 rtrules_db`
* topic-wordcountapp: 

  `spark-submit --class org.ant.examples.ESWordsCountApp --master mesos://192.168.33.10:7077 --deploy-mode cluster --driver-cores 0.5 --executor-memory 600M --total-executor-cores 7 spark-pruebas-assembly-1.0.jar node2:31000 192.168.33.1 27017 rtrules_db`
* topic-meetuprsvpapp: 

  `spark-submit --class org.ant.examples.MeetupRSVPAppES --master mesos://192.168.33.10:7077 --deploy-mode cluster --driver-cores 0.5 --executor-memory 600M --total-executor-cores 7 spark-pruebas-assembly-1.0.jar node2:31000 192.168.33.1 27017 rtrules_db`

Los parámetros de los programas son los siguientes:

* broker-list de kafka
* IP de mongodb
* Puerto de mongodb
* Base de datos de mongodb

