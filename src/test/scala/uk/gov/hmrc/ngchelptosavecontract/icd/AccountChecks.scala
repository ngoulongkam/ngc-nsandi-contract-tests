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

import org.scalatest.{Assertion, Matchers}
import play.api.libs.json.{Reads, __}
import uk.gov.hmrc.http.HttpResponse

trait AccountChecks extends Matchers {

  def checkAllFieldsPresentResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json

    (jsonBody \ "version").as[String] should not be empty
    (jsonBody \ "correlationId").as[String] should not be empty
    (jsonBody \ "accountNumber").as[String] should not be empty
    (jsonBody \ "availableWithdrawal").as[String] should not be empty
    (jsonBody \ "accountBalance").as[String] should not be empty
    (jsonBody \ "accountClosedFlag").as[String] should not be empty
    (jsonBody \ "accountClosureDate").as[String] should not be empty
    (jsonBody \ "accountClosingBalance").as[String] should not be empty
    (jsonBody \ "accountBlockingCode").as[String] should not be empty
    (jsonBody \ "accountBlockingReasonCode").as[String] should not be empty
    (jsonBody \ "currentInvestmentMonth" \ "investmentRemaining").as[String] should not be empty
    (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String] should not be empty
    (jsonBody \ "currentInvestmentMonth" \ "endDate").as[String] should not be empty
    (jsonBody \ "clientForename").as[String] should not be empty
    (jsonBody \ "clientSurname").as[String] should not be empty
    (jsonBody \ "dateOfBirth").as[String] should not be empty
    (jsonBody \ "addressLine1").as[String] should not be empty
    (jsonBody \ "addressLine2").as[String] should not be empty
    (jsonBody \ "addressLine3").as[String] should not be empty
    (jsonBody \ "addressLine4").as[String] should not be empty
    (jsonBody \ "addressLine5").as[String] should not be empty
    (jsonBody \ "postcode").as[String] should not be empty
    (jsonBody \ "countryCode").as[String] should not be empty
    (jsonBody \ "emailAddress").as[String] should not be empty
    (jsonBody \ "commsPreference").as[String] should not be empty
    (jsonBody \ "clientBlockingCode").as[String] should not be empty
    (jsonBody \ "clientBlockingReasonCode").as[String] should not be empty
    (jsonBody \ "clientCancellationStatus").as[String] should not be empty
    (jsonBody \ "nbaAccountNumber").as[String] should not be empty
    (jsonBody \ "nbaPayee").as[String] should not be empty
    (jsonBody \ "nbaRollNumber").as[String] should not be empty
    (jsonBody \ "nbaSortCode").as[String] should not be empty
    ((jsonBody \ "terms")(0) \ "termNumber").as[Int] should not be 0
    ((jsonBody \ "terms")(0) \ "startDate").as[String] should not be empty
    ((jsonBody \ "terms")(0) \ "endDate").as[String] should not be empty
    ((jsonBody \ "terms")(0) \ "maxBalance").as[String] should not be empty
    ((jsonBody \ "terms")(0) \ "bonusEstimate").as[String] should not be empty
    ((jsonBody \ "terms")(0) \ "bonusPaid").as[String] should not be empty
  }

  def checkNoVersionResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-002")
  }

  def checkInvalidVersionResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-003")
  }

  def checkNoNinoResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-004")
  }

  def checkInvalidNinoResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-005")
  }

  def checkNoAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-006")
  }

  def checkNoSystemIdResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-012")
  }

  def checkInvalidNinoAndVersionResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-003", "HTS-API015-005")
  }

  def checkClosedAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "accountClosedFlag").as[String] shouldBe "C"
    (response.json \ "accountClosureDate").as[String] should not be empty //KAM TODO: Assert date format
    (response.json \ "accountClosingBalance").as[String] should not be empty
  }

  def checkBlockedAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "clientBlockingCode").as[String] should not be "00"
    (response.json \ "clientBlockingReasonCode").as[String] should not be "00"
  }
}
