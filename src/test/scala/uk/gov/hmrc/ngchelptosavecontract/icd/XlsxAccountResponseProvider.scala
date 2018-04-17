/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ngchelptosavecontract.icd

import org.apache.poi.ss.usermodel.{CellType, Row, Workbook, WorkbookFactory}
import org.scalatest.exceptions.TestPendingException
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse

import scala.collection.JavaConverters._
import scala.util.matching.Regex

case class TestResult(testName: String, response: HttpResponse)

object XlsxAccountResponseProvider extends TestResponseProvider {

  override def noVersion: HttpResponse = Spreadsheet.response("Account-NoVersion")

  override def invalidVersion: HttpResponse = pending

  override def noNino: HttpResponse = Spreadsheet.response("Account-NoNino")

  override def invalidNino: HttpResponse = Spreadsheet.response("Account-NINOWithSPACING")

  override def invalidParams: HttpResponse = Spreadsheet.response("Account-InvalidParams")

  override def accountNotFound: HttpResponse = Spreadsheet.response("Account-NinoDoesNotExist")

  override def noSystemId: HttpResponse = Spreadsheet.response("Account-NoSystemId")

  override def noSystemIdNinoOrVersion: HttpResponse = Spreadsheet.response("Account-No-SysId-CorrelationID-NINO-Version")

  override def allFieldsPopulated: HttpResponse = pending

  override def allMandatoryFieldsPopulated: HttpResponse = pending

  override def closedAccount: HttpResponse = Spreadsheet.response("Account-ClosedAccount")

  override def blockedAccount: HttpResponse = Spreadsheet.response("Account-BlockedAccount")

  override def termNumbersFieldPopulated: HttpResponse = pending

  override def noBankDetailsAccount: HttpResponse = pending

  override def accountWithBalance: HttpResponse = pending

  override def accountWithCurrentInvestmentMonth: HttpResponse = pending

  override def accountWithZeroBalanceAndBonus: HttpResponse = Spreadsheet.response("Account-Retrieve-AllDetails-ZeroBalance")

  def pending = throw new TestPendingException

  private object Spreadsheet {
    def response(testName: String): HttpResponse = testResultsMap(testName)

    private val testResultsMap: Map[String, HttpResponse] = testResults.map(r => (r.testName, r.response)).toMap

    private def testResults: Seq[TestResult] = {
      val HeadingRowNum = 2 - 1
      val FirstDataRowNum = 3 - 1

      val workbook: Workbook = loadWorkbook

      val sheet = workbook.getSheetAt(0)
      val headingRow = sheet.getRow(HeadingRowNum)

      val statusPattern: Regex = """HTTP/1\.1 ([0-9]+) """.r

      def statusCode(cellValue: String): Int = {
        statusPattern.findFirstMatchIn(cellValue) match {
          case Some(mch) => mch.group(1).toInt
          case None => sys.error(s"Couldn't parse status code from: $cellValue")
        }
      }

      def find(row: Row, value: String) = {
        row.iterator().asScala
          .find(cell => cell.getCellTypeEnum == CellType.STRING && cell.getStringCellValue == value)
      }

      def findColumnIndex(name: String) = find(headingRow, name).get.getColumnIndex

      val testNameColumnIndex = findColumnIndex("Test Name")
      val jsonResponseColumnIndex = findColumnIndex("Json Response")
      val statusColumnIndex = findColumnIndex("Status")

      def rowIsNotEmpty(row: Row) = {
        !row.getCell(testNameColumnIndex).getStringCellValue.isEmpty
      }

      def testResultFromRow(row: Row) = {
        def cellString(columnIndex: Int) = row.getCell(columnIndex).getStringCellValue

        TestResult(
          cellString(testNameColumnIndex),
          HttpResponse(
            statusCode(cellString(statusColumnIndex)),
            Some(Json.parse(cellString(jsonResponseColumnIndex)))
          )
        )
      }

      sheet.rowIterator().asScala
        .filter(row => row.getRowNum >= FirstDataRowNum && rowIsNotEmpty(row))
        .map(testResultFromRow)
        .toSeq
    }

    private def loadWorkbook = {
      val resourceName = "/airgap/WebApi_Testing Update v1.0.xlsx"
      val inputStreamIfExists = Option(getClass.getResourceAsStream(resourceName))
      inputStreamIfExists.map { inputStream =>
        try {
          WorkbookFactory.create(inputStream)
        }
        finally {
          inputStream.close()
        }
      }.getOrElse(sys.error(s"Could not find resource $resourceName"))
    }
  }
}
