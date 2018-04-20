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

package uk.gov.hmrc.ngchelptosavecontract.icd.test

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import uk.gov.hmrc.ngchelptosavecontract.icd.checks.MessageChecks
import uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.message.{MessageResponseProvider, XlsxMessageResponseProvider}

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

class AirGapMessageSpec extends FeatureSpec with GivenWhenThen with Matchers with MessageChecks {
  private val responses: MessageResponseProvider = XlsxMessageResponseProvider // can be switched to between XlsxMessageResponseProvider and JsonFileMessageResponseProvider

  feature("iSIT air gap message JSON - CR20 scenarios") {
    scenario("No Message ID") {
      When("Get Message API is called without a messageId")
      val response = responses.noMessageId
      Then("404 - HTS-API015-007 error should be returned")
      checkNoMessageIdResponse(response)
    }

    scenario("Empty Version Number") {
      When("Get Message API is called with missing version number")
      val response = responses.missingVersionNumber
      Then("400 - HTS-API015-002 error should be returned")
      checkMissingVersionNumberResponse(response)
    }

    scenario("SystemID Field not sent") {
      When("Get Message API is called with no systemId")
      val response = responses.missingSystemId
      Then("400 - HTS-API015-012 error should be returned")
      checkMissingSystemIdResponse(response)
    }

//    This test will currently fail due to incorrect json format from spreadsheet (MSG05)
    scenario("Check all mandatory fields populated in Response") {
      When("A Message API with all mandatory fields populated")
      val response = responses.allMandatoryFieldsPopulated
      Then("Response should include all mandatory fields")
      checkAllMandatoryFieldsPopulated(response)
    }

    scenario("Null Authorization header") {
      When("Get Message API is called with null authorization header")
      val response = responses.nullAuthorizationHeader
      Then("401 HTS-API015-001 error should be returned")
      checkNullAuthorizationHeaderResponse(response)
    }

    scenario("Request with Invalid Message ID") {
      When("Get Message API is called with an invalid messageId")
      val response = responses.invalidMessageId
      Then("404 HTS-API015-009 error should be returned")
      checkInvalidMessageIdResponse(response)
    }
  }
}
