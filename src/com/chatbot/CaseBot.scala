package com.chatbot

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.util.concurrent.TimeUnit

class Connect() {

  private def loadDriver(): WebDriver = {
    System.setProperty("webdriver.gecko.driver", "/Users/nis/geckodriver");
    var driver = new FirefoxDriver();
    driver
  }

  def process() {
    val propertiesMap = new CaseBotConfig().loadProperty()

    // Loading SF    
    val sfdriver = loadDriver()
    var sf = new SalesforceConnector(sfdriver, propertiesMap)
    sf.login()
    val extractor = new SalesforceExtractor(sfdriver)
    val caseDataView = extractor.extractCaseData()
    println(caseDataView)

    // Loading HWX    
    val hwxdriver = loadDriver()
    var hwx = new HWXConnector(hwxdriver, propertiesMap)
    hwx.login()
    var hwxUploader = new HWXUploader(hwxdriver)
    hwxUploader.uploadCaseData(caseDataView)

  }
}

object CaseBot {

  def main(args: Array[String]) {
    val con = new Connect();
    con.process()

  }

}