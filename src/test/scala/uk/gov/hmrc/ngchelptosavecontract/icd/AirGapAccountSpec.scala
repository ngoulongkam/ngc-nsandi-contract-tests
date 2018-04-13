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
  private val responses: TestResponseProvider = XlsxAccountResponseProvider // can be switched to JsonFileResponseProvider

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
    }

    scenario("Closed Account - Ensure accountClosedFlag is set AND accountClosedDate and accountClosingBalance fields set") {
      Given("An account is closed")
      When("Get Account API is called")
      val response = responses.closedAccount
      Then("Response should include accountClosedFlag, accountClosedDate and accountClosingBalance")
      checkClosedAccountResponse(response)
    }

    //    TODO: this test fail as it doesn't make sense
    //    need to confirm if this scenario should be checking for accountBlockingCode and accountBlockingReasonCode
    //    If it is suppose to check for clientBlockingCode and clientBlockingReasonCode then there is a bug from Atos's json response
    scenario("Blocked account - Check Client Blocking Code & clientBlockingReason") {
      Given("An account is blocked")
      When("Get Account API is called")
      val response = responses.blockedAccount
      Then("Response should include clientBlockingCode and clientBlockingReasonCode")
      checkBlockedAccountResponse(response)
    }
  }
}
