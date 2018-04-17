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
    (jsonBody \ "accountClosedFlag").as[String] shouldBe ""
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
    ((jsonBody \ "terms") (0) \ "termNumber").as[Int] should not be 0
    ((jsonBody \ "terms") (0) \ "startDate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "endDate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "maxBalance").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "bonusEstimate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "bonusPaid").as[String] should not be empty
  }

  def checkAllMandatoryFieldsPresentResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json

    (jsonBody \ "version").as[String] should not be empty
    (jsonBody \ "accountNumber").as[String] should not be empty
    (jsonBody \ "availableWithdrawal").as[String] should not be empty
    (jsonBody \ "accountBalance").as[String] should not be empty
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
    (jsonBody \ "postcode").as[String] should not be empty
    (jsonBody \ "commsPreference").as[String] should not be empty
    (jsonBody \ "clientBlockingCode").as[String] should not be empty
    (jsonBody \ "clientBlockingReasonCode").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "termNumber").as[Int] should not be 0
    ((jsonBody \ "terms") (0) \ "startDate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "endDate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "maxBalance").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "bonusEstimate").as[String] should not be empty
    ((jsonBody \ "terms") (0) \ "bonusPaid").as[String] should not be empty
    ((jsonBody \ "terms") (1) \ "termNumber").as[Int] should not be 0
    ((jsonBody \ "terms") (1) \ "startDate").as[String] should not be empty
    ((jsonBody \ "terms") (1) \ "endDate").as[String] should not be empty
    ((jsonBody \ "terms") (1) \ "maxBalance").as[String] should not be empty
    ((jsonBody \ "terms") (1) \ "bonusEstimate").as[String] should not be empty
    ((jsonBody \ "terms") (1) \ "bonusPaid").as[String] should not be empty
  }

  def checkAllFieldsCharacterFormatResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json
    //TODO: check all comments in this method to check if <=7 character correct for 9(4)V9(2) format
    (jsonBody \ "version").as[String].length should be <= 6
    (jsonBody \ "correlationId").as[String].length should be <= 38
    (jsonBody \ "accountNumber").as[String].length should be <= 30
    (jsonBody \ "availableWithdrawal").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    (jsonBody \ "accountBalance").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    (jsonBody \ "accountClosedFlag").as[String].length should be <= 1
    (jsonBody \ "accountClosureDate").as[String].length should be <= 10
    (jsonBody \ "accountClosingBalance").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    (jsonBody \ "accountBlockingCode").as[String].length should be <= 2
    (jsonBody \ "accountBlockingReasonCode").as[String].length should be <= 2
    (jsonBody \ "currentInvestmentMonth" \ "investmentRemaining").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    (jsonBody \ "currentInvestmentMonth" \ "endDate").as[String].length should be <= 10
    (jsonBody \ "clientForename").as[String].length should be <= 26
    (jsonBody \ "clientSurname").as[String].length should be <= 300 //TODO: do we need 300 for surname?
    (jsonBody \ "dateOfBirth").as[String].length should be <= 10
    (jsonBody \ "addressLine1").as[String].length should be <= 35
    (jsonBody \ "addressLine2").as[String].length should be <= 35
    (jsonBody \ "addressLine3").as[String].length should be <= 35
    (jsonBody \ "addressLine4").as[String].length should be <= 35
    (jsonBody \ "addressLine5").as[String].length should be <= 35
    (jsonBody \ "postcode").as[String].length should be <= 10
    (jsonBody \ "countryCode").as[String].length should be <= 2
    (jsonBody \ "emailAddress").as[String].length should be <= 254
    (jsonBody \ "commsPreference").as[String].length should be <= 2
    (jsonBody \ "clientBlockingCode").as[String].length should be <= 2
    (jsonBody \ "clientBlockingReasonCode").as[String].length should be <= 2
    (jsonBody \ "clientCancellationStatus").as[String].length should be <= 1
    (jsonBody \ "nbaAccountNumber").as[String].length should be <= 8
    (jsonBody \ "nbaPayee").as[String].length should be <= 38
    (jsonBody \ "nbaRollNumber").as[String].length should be <= 18
    (jsonBody \ "nbaSortCode").as[String].length should be <= 6
    ((jsonBody \ "terms") (0) \ "termNumber").as[Int] should (be(1) or be(2))
    ((jsonBody \ "terms") (0) \ "startDate").as[String].length should be <= 10
    ((jsonBody \ "terms") (0) \ "endDate").as[String].length should be <= 10
    ((jsonBody \ "terms") (0) \ "maxBalance").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    ((jsonBody \ "terms") (0) \ "bonusEstimate").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
    ((jsonBody \ "terms") (0) \ "bonusPaid").as[String].length should be <= 7 //is 7 character correct for 9(4)V9(2) format
  }

  def checkFieldsWithRegexFormatResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json
    val moneyValuesRegex = "^\\d{1,}\\.\\d{2}$".r

    (jsonBody \ "availableWithdrawal").as[String] should fullyMatch regex moneyValuesRegex
    (jsonBody \ "accountBalance").as[String] should fullyMatch regex moneyValuesRegex
    (jsonBody \ "accountClosingBalance").as[String] should fullyMatch regex moneyValuesRegex
    (jsonBody \ "currentInvestmentMonth" \ "investmentRemaining").as[String] should fullyMatch regex moneyValuesRegex
    (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String] should fullyMatch regex moneyValuesRegex
    ((jsonBody \ "terms") (0) \ "maxBalance").as[String] should fullyMatch regex moneyValuesRegex
    ((jsonBody \ "terms") (0) \ "bonusEstimate").as[String] should fullyMatch regex moneyValuesRegex
    ((jsonBody \ "terms") (0) \ "bonusPaid").as[String] should fullyMatch regex moneyValuesRegex
  }

  def checkDateFieldsFormatResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val jsonBody = response.json
    val iso8601DateFormatRegex = "([0-9]{4}-[0-9]{2}-[0-9]{2})".r

    (jsonBody \ "accountClosureDate").as[String] should fullyMatch regex iso8601DateFormatRegex
    (jsonBody \ "currentInvestmentMonth" \ "endDate").as[String] should fullyMatch regex iso8601DateFormatRegex
    (jsonBody \ "dateOfBirth").as[String] should fullyMatch regex iso8601DateFormatRegex
    ((jsonBody \ "terms") (0) \ "startDate").as[String] should fullyMatch regex iso8601DateFormatRegex
    ((jsonBody \ "terms") (0) \ "endDate").as[String] should fullyMatch regex iso8601DateFormatRegex
  }

  def checkIncorrectAuthorizationHeaderResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 401
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-001")
  }

  def checkNullAuthorizationHeaderResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 401
    (response.json \ "errors").as[Seq[String]](Reads.seq((__ \ "errorMessageId").read[String])) shouldBe List("HTS-API015-001")
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
    (response.json \ "errors").as[Set[String]](Reads.set((__ \ "errorMessageId").read[String])) shouldBe Set("HTS-API015-003", "HTS-API015-005")
  }

  def checkNoSystemIdNinoOrVersionResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Set[String]](Reads.set((__ \ "errorMessageId").read[String])) shouldBe Set("HTS-API015-002", "HTS-API015-004", "HTS-API015-012")
  }

  def checkInvalidParamResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 400
    (response.json \ "errors").as[Set[String]](Reads.set((__ \ "errorMessageId").read[String])) shouldBe Set("HTS-API015-005", "HTS-API015-003")
  }

  def checkClosedAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "accountClosedFlag").as[String] shouldBe "C"
    (response.json \ "accountClosureDate").as[String] should not be empty
    (response.json \ "accountClosingBalance").as[String] should not be empty
  }

  def checkBlockedAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "accountBlockingCode").as[String] should not be "00"
    (response.json \ "accountBlockingReasonCode").as[String] should not be "00"
    (response.json \ "clientBlockingCode").as[String] shouldBe "00"
    (response.json \ "clientBlockingReasonCode").as[String] shouldBe "00"
  }

  def checkCorrectTermNumberPresentResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    ((response.json \ "terms") (0) \ "termNumber").as[Int] shouldBe 1
    ((response.json \ "terms") (1) \ "termNumber").as[Int] shouldBe 2
  }

  def checkNoBankDetailsAccountResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    response.body should not include "nbaAccountNumber"
    response.body should not include "nbaPayee"
    response.body should not include "nbaRollNumber"
    response.body should not include "nbaSortCode"
  }

  def checkBalanceFieldResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "accountBalance").as[String] shouldBe "45.12"
  }

  def checkCurrentInvestmentMonthResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "currentInvestmentMonth" \ "investmentRemaining").as[String] shouldBe "43.00"
    (response.json \ "currentInvestmentMonth" \ "investmentRemaining").as[String] should not be "50.00"
  }

  def checkZeroBalanceAndBonusFieldResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "availableWithdrawal").as[String] shouldBe "0.00"
    (response.json \ "accountBalance").as[String] shouldBe "0.00"
    ((response.json \ "terms") (0) \ "maxBalance").as[String] shouldBe "0.00"
    ((response.json \ "terms") (0) \"bonusEstimate").as[String] shouldBe "0.00"
    ((response.json \ "terms") (0) \"bonusPaid").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \ "maxBalance").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \"bonusEstimate").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \"bonusPaid").as[String] shouldBe "0.00"
  }

  def checkUKPostcodeFieldResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "postcode").as[String] shouldBe "FY1 5QY"
    (response.json \ "countryCode").as[String] shouldBe "GB"
  }

  def checkAccountWithNbaRollNumberFieldResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "nbaRollNumber").as[String] should not be empty
    (response.json \ "nbaSortCode").as[String] should not be empty
    (response.json \ "nbaRollNumber").as[String] shouldBe "A1234567AAA"
    (response.json \ "nbaSortCode").as[String] shouldBe "202020"
  }

  def checkAccountPaidInMaxForTheMonthResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "currentInvestmentMonth" \ "investmentRemaining").as[String] shouldBe "0.00"
  }

  def checkAccountWithZeroBalanceResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "availableWithdrawal").as[String] shouldBe "0.00"
    (response.json \ "accountBalance").as[String] shouldBe "0.00"
    (response.json \ "currentInvestmentMonth" \ "investmentRemaining").as[String] shouldBe "50.00"
    ((response.json \ "terms") (0) \ "maxBalance").as[String] shouldBe "0.00"
    ((response.json \ "terms") (0) \ "bonusEstimate").as[String] shouldBe "0.00"
    ((response.json \ "terms") (0) \ "bonusPaid").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \ "maxBalance").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \ "bonusEstimate").as[String] shouldBe "0.00"
    ((response.json \ "terms") (1) \ "bonusPaid").as[String] shouldBe "0.00"
  }

  def checkAccountWithNoCorrelationIdResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "correlationId").as[String] should not be empty
  }

  def checkAccountWithChannelIslandsPostcodeResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "postcode").as[String] should startWith ("GY")
    (response.json \ "countryCode").as[String] shouldBe "GB"
  }

  def checkAccountWithIsleOfManPostcodeResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    (response.json \ "postcode").as[String] should startWith ("IM")
    (response.json \ "countryCode").as[String] shouldBe "GB"
  }

  def checkAccountWith1stTermBonusNotYetBeenPaidResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    ((response.json \ "terms") (0) \ "bonusEstimate").as[String] should not be "0.00"
    ((response.json \ "terms") (0) \ "bonusPaid").as[String] shouldBe "0.00"
  }

  def checkAccountWith2ndTermBonusNotYetBeenPaidResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    ((response.json \ "terms") (1) \ "bonusEstimate").as[String] should not be "0.00"
    ((response.json \ "terms") (1) \ "bonusPaid").as[String] shouldBe "0.00"
  }

  def checkAccountWith1stTermBonusPaidResponse(response: HttpResponse): Assertion = {
    response.status shouldBe 200
    val bonusAmount = "109.92"
    ((response.json \ "terms") (0) \ "bonusEstimate").as[String] should not be "0.00"
    ((response.json \ "terms") (0) \ "bonusPaid").as[String] should not be "0.00"
    ((response.json \ "terms") (0) \ "bonusEstimate").as[String] shouldBe bonusAmount
    ((response.json \ "terms") (0) \ "bonusPaid").as[String] shouldBe bonusAmount
  }
}
