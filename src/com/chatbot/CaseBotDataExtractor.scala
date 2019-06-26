package com.chatbot

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

trait DataExtractor {

  def extractCaseData(): List[Map[String, String]]

}

class SalesforceExtractor(webDriver: WebDriver) extends DataExtractor {

  def extractCaseData(): List[Map[String, String]] = {

    var caseList = new ListBuffer[Map[String, String]]()

    val tableElement = By.xpath("/html/body/div[1]/div[3]/table/tbody/tr/td[2]/div[1]/div[1]/form/div[3]/div[4]/div/div/div/div[1]/div[2]/div/div")
    val tableElementWait = new WebDriverWait(webDriver, 90)
    tableElementWait.until(ExpectedConditions.visibilityOfElementLocated(tableElement))
    var table_rows = webDriver.findElements(tableElement)
    println(s"No# of cases: ${table_rows.size()}")

    for (rowIndex <- 1 to table_rows.size()) {
      println(s"processing the rec: $rowIndex")
      val extractedPropertyMap: Map[String, String] = Map.empty[String, String]

      val xpath = "/html/body/div[1]/div[3]/table/tbody/tr/td[2]/div[1]/div[1]/form/div[3]/div[4]/div/div/div/div[1]/div[2]/div/div[" + rowIndex + "]"
      val caseNoLink = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[4]/div/a"))

      val caseNo = caseNoLink.getText
      val severityLevel = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[5]/div")).getText.charAt(0).toString()
      val productName = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[6]/div/a/span")).getText
      val accountName = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[7]/div/a/span")).getText
      val subject = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[9]/div")).getText
      val timeOpened = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[10]/div")).getText
      val status = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[11]/div")).getText
      val internalStatus = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[12]/div")).getText
      val lastCustomerUpdateTime = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[13]/div")).getText
      val lastModifiedTime = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[14]/div")).getText
      val ownerName = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[15]/div")).getText
      val blueDiamondAccount = webDriver.findElement(By.xpath(xpath + "/table/tbody/tr/td[16]/div")).getText

      extractedPropertyMap += ("caseNo" -> caseNo)
      extractedPropertyMap += ("severityLevel" -> severityLevel)
      extractedPropertyMap += ("productName" -> productName)
      extractedPropertyMap += ("accountName" -> accountName)
      extractedPropertyMap += ("timeOpened" -> timeOpened)
      extractedPropertyMap += ("status" -> status)
      extractedPropertyMap += ("internalStatus" -> internalStatus)
      extractedPropertyMap += ("lastCustomerUpdateTime" -> lastCustomerUpdateTime)
      extractedPropertyMap += ("lastModifiedTime" -> lastModifiedTime)
      extractedPropertyMap += ("ownerName" -> ownerName)
      extractedPropertyMap += ("blueDiamondAccount" -> blueDiamondAccount)

      caseNoLink.click()
      webDriver.findElement(By.xpath("/html/body/div[1]/div[3]/table/tbody/tr/td[2]/div[2]/div[3]/div[1]/div[1]/ul/li[2]/a")).click()

      val iframeElement = By.xpath("//iframe")
      val allIFrames = webDriver.findElements(iframeElement)
      var mustGatherIFrame: WebElement = null
      allIFrames.forEach(elem => {
        if (elem.getAttribute("title").equals("MustGatherResponseEntry")) {
          mustGatherIFrame = elem
        }
      })
      var frameDriver = webDriver.switchTo().frame(mustGatherIFrame)

      if (productName.equals("Big SQL")) {

        //val productVersion /html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[1]/td/div
        //if(

        val productComponent = "TODO - Provide DefaultVersion"
        val osType = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[2]/td/div")).getText

        val deploymentPhase = "TODO - Provide DefaultVersion"
        val productVersion = "TODO - Provide DefaultVersion"
        val ambariVersion = "TODO - Provide DefaultVersion"

        extractedPropertyMap += ("productComponent" -> productComponent)
        extractedPropertyMap += ("osType" -> osType)
        extractedPropertyMap += ("deploymentPhase" -> deploymentPhase)
        extractedPropertyMap += ("productVersion" -> productVersion)
        extractedPropertyMap += ("ambariVersion" -> ambariVersion)

      } else {

        val productComponent = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[1]/td/div")).getText
        val osType = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[2]/td/div")).getText
        val deploymentPhase = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[3]/td/div")).getText
        val productVersion = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[4]/td/div")).getText
        val ambariVersion = webDriver.findElement(By.xpath("/html/body/form/div[1]/div/div/div/div[2]/div/div/table/tbody/tr[5]/td/div")).getText

        extractedPropertyMap += ("productComponent" -> productComponent)
        extractedPropertyMap += ("osType" -> osType)
        extractedPropertyMap += ("deploymentPhase" -> deploymentPhase)
        extractedPropertyMap += ("productVersion" -> productVersion)
        extractedPropertyMap += ("ambariVersion" -> ambariVersion)

      }

      frameDriver.switchTo().defaultContent()
      caseList += extractedPropertyMap
      webDriver.navigate().back()

    }
    println(caseList)
    caseList.toList

  }
}