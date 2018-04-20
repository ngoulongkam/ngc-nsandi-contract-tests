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

package uk.gov.hmrc.ngchelptosavecontract.icd.checks

import org.scalatest.{Assertion, Matchers}
import play.api.libs.json._
import uk.gov.hmrc.http.HttpResponse

trait MessagesChecks extends Matchers {

  def checkInvalidNinoResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-006")
  }

  def checkMissingVersionNumberResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-002")
  }

  def checkIncorrectAuthorizationHeader(response: HttpResponse): Assertion = {
    response.status shouldBe 401
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-001")
  }

  def checkMissingSystemIdResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-012")
  }

  def checkAllMandatoryFieldsPopulated(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json

    (jsonBody \ "version").as[String] should not be empty
    ((jsonBody \ "messages") (0) \ "messageId").as[String] should not be empty
    ((jsonBody \ "messages") (0) \ "title").as[String] should not be empty
    ((jsonBody \ "messages") (0) \ "subject").as[String] should not be empty
    ((jsonBody \ "messages") (0) \ "creationDateTime").as[String] should not be empty
    ((jsonBody \ "messages") (0) \ "readIndicator").as[Boolean] should (be(true) or be(false))
    ((jsonBody \ "messages") (0) \ "sendingMethod").as[String] should not be empty
  }

  def checkAccountWithNoMessages(response: HttpResponse): Assertion = {
    response.status shouldBe 200

    response.body should not include "messageId"
    response.body should not include "title"
    response.body should not include "subject"
    response.body should not include "creationDateTime"
    response.body should not include "readIndicator"
    response.body should not include "sendingMethod"
  }

  def checkAccountWithMultipleReadMessagesResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200

    (Json.parse(response.body) \\ "readIndicator").foreach(_.as[Boolean] shouldBe true)
    val listOfAllMessageId = (Json.parse(response.body) \\ "messageId").map(_.as[String])
    listOfAllMessageId.size should be > 1
  }
}