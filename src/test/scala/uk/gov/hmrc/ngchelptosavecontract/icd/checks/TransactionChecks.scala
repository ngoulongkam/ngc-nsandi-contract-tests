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
import play.api.libs.json.{Reads, __}
import uk.gov.hmrc.http.HttpResponse

trait TransactionChecks extends Matchers {

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
}
