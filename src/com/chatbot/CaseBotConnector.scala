package com.chatbot

import java.util.concurrent.TimeUnit

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

trait Connector {

  /**
   * Login implemention that login to the specific platform.
   */
  def login()

}

class SalesforceConnector(webDriver: WebDriver, connectionPropertiesMap: Map[String, String]) extends Connector {

  def login() {
    webDriver.get(connectionPropertiesMap("sf.url"));
    webDriver.manage().window().maximize();
    webDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS)
    webDriver.findElement(By.name("username")).sendKeys(connectionPropertiesMap("sf.user"));
    webDriver.findElement(By.name("password")).sendKeys(connectionPropertiesMap("sf.password"));
    webDriver.findElement(By.name("password")).submit();

    webDriver.findElement(By.xpath("/html/body/div[1]/div[3]/table/tbody/tr/td[2]/div[1]/div[1]/form/div[1]/div/select/option[text()='" + connectionPropertiesMap("sf.queueName") + "']")).click()
  }

}

class HWXConnector(webDriver: WebDriver, connectionPropertiesMap: Map[String, String]) extends Connector {

  def login() {
    webDriver.get(connectionPropertiesMap("hwx.url"));
    webDriver.manage().window().maximize();
    webDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS)
    webDriver.findElement(By.name("j_id0:j_id2:username")).sendKeys(connectionPropertiesMap("hwx.user"));
    webDriver.findElement(By.name("j_id0:j_id2:password")).sendKeys(connectionPropertiesMap("hwx.password"));
    webDriver.findElement(By.name("j_id0:j_id2:j_id38")).click();

  }

}

