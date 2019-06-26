package com.chatbot

import scala.io.Source

class CaseBotConfig {
  def loadProperty(): Map[String, String] = {

    val casebot_home = System.getProperty("CASEBOT_HOME")

    val propertiesMap = Source.fromFile(casebot_home + "casebot.properties").getLines.map(_.trim).filter(!_.startsWith("#")).filter(_.contains("=")).map(line => {
      val Array(key, value) = line.split("=", 2)
      (key.trim, value.trim)
    }).toMap

    propertiesMap
  }
}

object CaseBotConfig extends App {

  val sfdriver = new CaseBotConfig().loadProperty()
  println(sfdriver)

}
