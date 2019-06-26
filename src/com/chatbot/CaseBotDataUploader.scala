package com.chatbot

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import scala.collection.JavaConverters._
import util.control.Breaks._

import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.FluentWait

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement

import java.util.function.Function
import org.openqa.selenium.support.ui.WebDriverWait
import scala.collection.mutable.ListBuffer
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import scala.collection.mutable.Map

trait DataUploader {

  def uploadCaseData(sfCases: List[Map[String, String]])

}

class HWXUploader(webDriver: WebDriver) extends DataUploader {

  override def uploadCaseData(sfCases: List[Map[String, String]]) = {
    
    val caseDataView = sfCases(0)

    val tableElement = By.cssSelector("table.cLiveAgentChatButton a")
    //val tableElementWait = new WebDriverWait(webDriver, 60)
    //tableElementWait.until(ExpectedConditions.visibilityOfElementLocated(tableElement))
    var newCase = webDriver.findElement(tableElement)
    newCase.click()

    val custName = getElementFromFrame(By.name("page:form1:input-38"))
    custName.sendKeys(caseDataView("accountName"))
    webDriver.switchTo().defaultContent()

    val probStatement = getElementFromFrame(By.name("page:form1:input-33"))
    probStatement.sendKeys(s"[IBM ${caseDataView("caseNo")}] - ${caseDataView("subject")}")
    webDriver.switchTo().defaultContent()

    val probDetails = getElementFromFrame(By.name("page:form1:input-34"))
    probDetails.sendKeys("TODO - Need To be extracted")
    webDriver.switchTo().defaultContent()

    val severityStr = caseDataView("severityLevel") match {
      case "1" => "S1 - Production Down"
      case "2" => "S2 - Core Feature Inoperative"
      case "3" => "S3 - Minor Feature Inoperative"
      case "4" => "S4 - Request for Information"

    }

    val selectElement = By.name("page:form1:input-35")
    val severityElem = getElementFromFrame(selectElement)
    var sel = new Select(severityElem)
    sel.selectByValue(severityStr);
    webDriver.switchTo().defaultContent()

    Thread.sleep(1000 * 5)

    val next = getElementFromFrame(By.name("page:form1:j_id110"))
    next.click()
    //webDriver.switchTo().defaultContent()

  }

  def getElementFromFrame(by: By): WebElement = {
    val frame = webDriver.findElement(By.tagName("iframe"))
    var element: WebElement = null
    webDriver.switchTo().frame(frame)
    if (webDriver.findElement(by).isDisplayed())
      element = webDriver.findElement(by)
    //webDriver.switchTo().defaultContent()
    element
  }

}


