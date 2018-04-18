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

package uk.gov.hmrc.ngchelptosavecontract.support

import org.apache.poi.ss.usermodel.{CellType, Row, Workbook, WorkbookFactory}
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.Resources.withResource

import scala.collection.JavaConverters._
import scala.util.matching.Regex

case class TestResult(testName: String, status: Int, responseBody: String)

object AirGapSpreadsheet {
  def response(testName: String): HttpResponse = {
    val testResult = testResultsMap(testName)
    HttpResponse(testResult.status, Some(Json.parse(testResult.responseBody)))
  }

  private val testResultsMap: Map[String, TestResult] = testResults.map(r => (r.testName, r)).toMap

  private def testResults: Seq[TestResult] = {
    val HeadingRowNum = 1 - 1
    val FirstDataRowNum = 2 - 1

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

    def findColumnIndex(name: String) = find(headingRow, name).getOrElse(sys.error(s"Couldn't find column with name $name")).getColumnIndex

    val testNameColumnIndex = findColumnIndex("Test Name")
    val jsonResponseColumnIndex = findColumnIndex("Json Response")
    val statusColumnIndex = findColumnIndex("Raw Response Status")

    def rowIsNotEmpty(row: Row) = {
      !row.getCell(testNameColumnIndex).getStringCellValue.isEmpty
    }

    def testResultFromRow(row: Row) = {
      def cellString(columnIndex: Int) = row.getCell(columnIndex).getStringCellValue

      TestResult(
        cellString(testNameColumnIndex),
        statusCode(cellString(statusColumnIndex)),
        cellString(jsonResponseColumnIndex)
      )
    }

    sheet.rowIterator().asScala
      .filter(row => row.getRowNum >= FirstDataRowNum && rowIsNotEmpty(row))
      .map(testResultFromRow)
      .toSeq
  }

  private def loadWorkbook: Workbook =
    withResource("/airgap/WebApiTestingReport_17042018.xlsx")(WorkbookFactory.create)
}
