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

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * Tests that check that the air gap JSON files for NS&I's
  * /nsi-services/account endpoint meet the full contract defined in the ICD
  * (even the parts that we do not currently rely on).
  *
  * We can only check the body, not the HTTP status code or headers, because the
  * JSON files contain only the body.
  *
  * Scenario names are taken from "CR20 scenarios INTERNAL.xlsx" in Google Drive
  */
class AirGapAccountSpec extends FeatureSpec with GivenWhenThen with Matchers with AccountChecks {
  private val responses: TestResponseProvider = JsonFileResponseProvider // can be switched to JsonFileResponseProvider

  feature("iSIT air gap account JSON - CR20 scenarios") {
    scenario("Non-Existent Version Number/Empty string") {
      When("Get Account API is called without a version parameter")
      val response = responses.noVersion
      Then("400 HTS-API015-002 error should be returned")
      checkNoVersionResponse(response)
    }

    scenario("Invalid (unsupported) version number") {
      When("Get Account API is called with an invalid version")
      val response = responses.invalidVersion
      Then("400 HTS-API015-003 error should be returned")
      checkInvalidVersionResponse(response)
    }

    scenario("Invalid parameters") {
      When("Get Account API is called with an invalid parameter")
      val response = responses.invalidParams
      Then("400 containing multiple errors should be returned")
      checkInvalidParamResponse(response)
    }

    scenario("Invalid Nino / Empty String") {
      When("Get Account API is called without a nino parameter")
      val noNinoResponse = responses.noNino
      Then("400 HTS-API015-004 error should be returned")
      checkNoNinoResponse(noNinoResponse)

      When("Get Account API is called with an empty nino parameter")
      val invalidNinoResponse = responses.invalidNino
      Then("400 HTS-API015-005 error should be returned")
      checkInvalidNinoResponse(invalidNinoResponse)

      When("Get Account API is called with a nino for which no Help to Save account exists")
      val noAccountResponse = responses.accountNotFound
      Then("400 HTS-API015-006 error should be returned")
      checkNoAccountResponse(noAccountResponse)
    }

    scenario("Incorrect SystemID/Empty String/ Field not sent") {
      When("Get Account API is called with no systemId parameter")
      val response = responses.noSystemId
      Then("400 HTS-API015-012 error should be returned")
      checkNoSystemIdResponse(response)
    }

    scenario("Check Error Codes,Error Statuses and messages in response as per ICD") {
      When("Get Account API is called and there are multiple validation errors for the parameters")
      val response = responses.noSystemIdNinoOrVersion
      Then("400 containing multiple errors should be returned")
      checkNoSystemIdNinoOrVersionResponse(response)
    }

    scenario("Check all fields are present in response mandatory and non mandatory") {
      Given("An account with all fields populated")
      When("Get Account API is called")
      val response = responses.allFieldsPopulated
      Then("Response should include all fields")
      checkAllFieldsPresentResponse(response)
    }

    scenario("Closed Account - Ensure accountClosedFlag is set AND accountClosedDate and accountClosingBalance fields set") {
      Given("An account is closed")
      When("Get Account API is called")
      val response = responses.closedAccount
      Then("Response should include accountClosedFlag, accountClosedDate and accountClosingBalance")
      checkClosedAccountResponse(response)
    }

    // TODO: test a variety of blocked accounts and clients and check that the correct values are in the
    // accountBlockingCode, accountBlockingReasonCode, clientBlockingCode and clientBlockingReasonCode
    // fields (not just whether they are "00" or not)
    scenario("Blocked account - Check Client Blocking Code & clientBlockingReason") {
      Given("An account is blocked (account blocking, not client blocking")
      When("Get Account API is called")
      val blockedAccountResponse = responses.blockedAccount
      Then("Response should include accountBlockingCode and accountBlockingReasonCode")
      checkBlockedAccountResponse(blockedAccountResponse)

      Given("An account is blocked (client blocking, not account blocking")
      pending
    }

    scenario("Check all mandatory fields populated in Response") {
      Given("An account with all mandatory fields populated")
      val response = responses.allMandatoryFieldsPopulated
      Then("Response should include all mandatory fields")
      checkAllMandatoryFieldsPresentResponse(response)
    }

    scenario("Term1/Term2 customer and check Term Numbers are set correctly") {
      Given("An account with Term1/Term2 fields populated")
      val response = responses.termNumbersFieldPopulated
      Then("Response should include the correct term number")
      checkCorrectTermNumberPresentResponse(response)
    }
  }
}
