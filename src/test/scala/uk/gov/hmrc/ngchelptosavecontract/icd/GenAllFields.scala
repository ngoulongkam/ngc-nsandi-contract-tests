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

import java.io.FileInputStream

import play.api.libs.json.{JsObject, JsValue, Json}

object GenAllFields {

  def main(args: Array[String]): Unit = {
    val schema = loadJson("../mobile-help-to-save-apis/1.1/get_account_by_nino/V1.0/get_account_by_nino_RESP_schema_V1.0.json")
    generateSpec(schema)
  }

  private def loadJson(filename: String): JsValue = {
    val inputStream = new FileInputStream(filename)
    try {
      Json.parse(inputStream)
    }
    finally {
      inputStream.close()
    }
  }

  def generateSpec(schema: JsValue): Unit = {
    val propertiesJson = (schema \ "properties").as[JsObject]
    generateChecksForProperties(Seq.empty, propertiesJson)
  }

  private def generateChecksForProperties(path: Seq[String], propertiesJson: JsObject, extraLookup: String = ""): Unit = {
    propertiesJson.keys.foreach { propertyName =>
      val propertyJson = (propertiesJson \ propertyName).as[JsObject]
      val propertyType = (propertyJson \ "type").as[String]

      val pathLookup = path.foldLeft("")((a, b) => s"""$a \\ "$b"""")
      def lookup = s"""(jsonBody$pathLookup \\ "$propertyName")$extraLookup"""
      if (propertyType == "string") {
        println(s"""$lookup.as[String] should not be empty""")
      } else if (propertyType == "integer") {
        println(s"""$lookup.as[Int] should not be 0""")
      } else if (propertyType == "array") {
        val arrayPath = path :+ propertyName
        val arrayPropertiesJson = (propertyJson \ "items" \ "properties").as[JsObject]
        // this will generate incorrect code for objects or arrays nested
        // within arrays, but the schema we're dealing with doesn't have any
        // of them
        generateChecksForProperties(arrayPath, arrayPropertiesJson, "(0)")
      } else if (propertyType == "object") {
        val arrayPath = path :+ propertyName
        val arrayPropertiesJson = (propertyJson \ "properties").as[JsObject]
        generateChecksForProperties(arrayPath, arrayPropertiesJson)
      } else {
        println(s"""Unknown property type: $propertyType""")
      }
    }
  }
}
