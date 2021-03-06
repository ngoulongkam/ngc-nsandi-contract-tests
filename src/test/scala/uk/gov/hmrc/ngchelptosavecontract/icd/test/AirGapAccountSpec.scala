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
import uk.gov.hmrc.ngchelptosavecontract.icd.checks.AccountChecks
import uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.account.{AccountResponseProvider, XlsxAccountResponseProvider}

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
  private val responses: AccountResponseProvider = XlsxAccountResponseProvider // can be switched to between XlsxAccountResponseProvider and JsonFileAccountResponseProvider

  feature("iSIT air gap account JSON - CR20 scenarios") {
    scenario("Incorrect Authorization header") {
      When("Get Account API is called with incorrect authorization header")
      val response = responses.incorrectAuthorizationHeader
      Then("401 HTS-API015-001 error should be returned")
      checkIncorrectAuthorizationHeaderResponse(response)
    }

    scenario("Null Authorization header") {
      When("Get Account API is called with null authorization header")
      val response = responses.nullAuthorizationHeader
      Then("401 HTS-API015-001 error should be returned")
      checkNullAuthorizationHeaderResponse(response)
    }

    scenario("Empty Version Number/Empty string") {
      When("Get Account API is called without a version parameter")
      val response = responses.noVersion
      Then("400 HTS-API015-002 error should be returned")
      checkNoVersionResponse(response)
    }

    scenario("Invalid Version ID") {
      When("Get Account API is called with an invalid version")
      val response = responses.invalidVersion
      Then("400 HTS-API015-003 error should be returned")
      checkInvalidVersionResponse(response)
    }

    scenario("Request with Invalid Params") {
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

    scenario("SystemID Field not sent") {
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

    scenario("Check Data format for each field as per ICD") {
      Given("An account with all fields populated")
      val response = responses.allFieldsPopulated
      Then("The character format of all fields in response as per ICD")
      checkAllFieldsCharacterFormatResponse(response)
      And("The regex format of fields in response as per ICD")
      checkFieldsWithRegexFormatResponse(response)
      And("The date format of fields in response as per ICD")
      checkDateFieldsFormatResponse(response)
    }

    scenario("Account opened - no bank details added") {
      Given("An account opened with no bank details")
      val response = responses.noBankDetailsAccount
      Then("200 - Bank details should be omitted from the response")
      checkNoBankDetailsAccountResponse(response)
    }

    scenario("Customer with non zero balance") {
      Given("An account with non zero balance")
      val response = responses.accountWithBalance
      Then("200 - Response should include a balance")
      checkBalanceFieldResponse(response)
    }

    scenario("Customer with non zero amount paid in current month") {
      Given("An account with non zero amount paid in current month")
      val response = responses.accountWithCurrentInvestmentMonth
      Then("200 - Response should include an amount paid currentInvestmentMonth")
      checkCurrentInvestmentMonthResponse(response)
    }

    scenario("Customer who has zero balance and zero bonus") {
      Given("An account has zero balance and zero bonus")
      val response = responses.accountWithZeroBalanceAndBonus
      Then("200 - Balance and bonus should have zero in the response")
      checkZeroBalanceAndBonusFieldResponse(response)
    }

    scenario("Check customer with UK Post code") {
      Given("An account with a UK Post code")
      val response = responses.accountWithUKPostcode
      Then("200 - UK Post code should be in the response")
      checkUKPostcodeFieldResponse(response)
    }

    scenario("Check Account with Building Society as bank details") {
      Given("An account with Building Society as bank details")
      val response = responses.accountWithBuildingSocietyBankDetails
      Then("200 - nbaRollNumber should be included in the response")
      checkAccountWithNbaRollNumberFieldResponse(response)
    }

    scenario("Customer with No headroom(paid in max for the month)") {
      Given("An account paid in max for the month")
      val response = responses.accountPaidInMaxForTheMonth
      Then("200 - investmentRemaining should be 0 in the response")
      checkAccountPaidInMaxForTheMonthResponse(response)
    }

    scenario("Customer with Zero balance") {
      Given("An account with zero balance")
      val response = responses.accountWithZeroBalance
      Then("200 - All balance related fields should be 0.00")
      checkAccountWithZeroBalanceResponse(response)
    }

    scenario("Request with no correllation ID") {
      Given("Calling Account API with no correlationId")
      val response = responses.accountWithNoCorrelationId
      Then("200 - Response should return a new correlationId")
      checkAccountWithNoCorrelationIdResponse(response)
    }

    scenario("Check customer with Channel Islands Post code") {
      Given("An account with Channel Islands Post code")
      val response = responses.accountWithChannelIslandsPostcode
      Then("200 - GY Post code should be included in the field")
      checkAccountWithChannelIslandsPostcodeResponse(response)
    }

    scenario("Check customer with IOM post code") {
      Given("An account with Isle of Man Post code")
      val response = responses.accountWithIsleOfManPostcode
      Then("200 - IM Post code should be included in the field")
      checkAccountWithIsleOfManPostcodeResponse(response)
    }

    scenario("Customer who has estimated 1st term bonus greater than zero but bonus not yet paid") {
      Given("An account with 1st term bonus but its not yet paid")
      val response = responses.accountWith1stTermBonusNotYetBeenPaid
      Then("200 - bonusEstimate should be more than 0 and bonusPaid should be 0 in the response")
      checkAccountWith1stTermBonusNotYetBeenPaidResponse(response)
    }

    scenario("Customer who has estimated 2nd term bonus greater than zero but bonus not yet paid") {
      Given("An account with 2nd term bonus but its not yet paid")
      val response = responses.accountWith2ndTermBonusNotYetBeenPaid
      Then("200 - 2nd term bonusEstimate should be more than 0 and bonusPaid should be 0 in the response")
      checkAccountWith2ndTermBonusNotYetBeenPaidResponse(response)
    }

    scenario("Customer who has estimated 1st term bonus greater than zero and bonus paid") {
      Given("An account with 1st term bonus and it is paid")
      val response = responses.accountWith1stTermBonusPaid
      Then("200 - bonusEstimate should be more than 0 and bonusPaid should same as bonusEstimate")
      checkAccountWith1stTermBonusPaidResponse(response)
    }

    scenario("Check Account-where-MaxFirstBonusReached") {
      Given("An account have reached maximum first term bonus")
      val response = responses.accountWithMaxFirstTerm
      Then("200 - Maximum bonus reached in first term response")
      checkAccountWithMaxFirstTermResponse(response)
    }
  }
}