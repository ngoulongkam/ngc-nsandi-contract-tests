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
import uk.gov.hmrc.ngchelptosavecontract.icd.checks.TransactionChecks
import uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.transaction.{TransactionResponseProvider, XlsxTransactionResponseProvider}

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

class AirGapTransactionSpec extends FeatureSpec with GivenWhenThen with Matchers with TransactionChecks {
  private val responses: TransactionResponseProvider = XlsxTransactionResponseProvider // can be switched to between XlsxTransactionResponseProvider and JsonFileTransactionResponseProvider

  feature("iSIT air gap transaction JSON - CR20 scenarios") {
    scenario("Invalid Nino / Request with no Nino / Request with  Nino with spaces") {
      When("Get Transaction API is called with invalid nino")
      val response = responses.invalidNino
      Then("400 - HTS-API015-006 error should be returned")
      checkInvalidNinoResponse(response)
    }

    scenario("Empty Version Number") {
      When("Get Transaction API is called with missing version number")
      val response = responses.missingVersionNumber
      Then("400 - HTS-API015-002 error should be returned")
      checkMissingVersionNumberResponse(response)
    }

    scenario("Incorrect Authorization header") {
      When("Get Transaction API is called with incorrect authorization header")
      val response = responses.incorrectAuthorizationHeader
      Then("401 - HTS-API015-001 error should be returned")
      checkIncorrectAuthorizationHeader(response)
    }

    scenario("SystemID Field not sent") {
      When("Get Transaction API is called with no systemId")
      val response = responses.missingSystemId
      Then("400 - HTS-API015-012 error should be returned")
      checkMissingSystemIdResponse(response)
    }

//    This test currently failed because the json body for transaction should return "accountDate", NOT "accountingDate"
    scenario("Check all mandatory fields populated in Response") {
      When("A Transaction API with all mandatory fields populated")
      val response = responses.allMandatoryFieldsPopulated
      Then("Response should include all mandatory fields")
      checkAllMandatoryFieldsPopulated(response)
    }
  }
}
