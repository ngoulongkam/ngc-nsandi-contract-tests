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
import uk.gov.hmrc.ngchelptosavecontract.icd.checks.MessagesChecks
import uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.messages.{MessagesResponseProvider, XlsxMessagesResponseProvider}

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

class AirGapMessagesSpec extends FeatureSpec with GivenWhenThen with Matchers with MessagesChecks {
  private val responses: MessagesResponseProvider = XlsxMessagesResponseProvider // can be switched to between XlsxMessagesResponseProvider and JsonFileMessagesResponseProvider

  feature("iSIT air gap messages JSON - CR20 scenarios") {
    scenario("pending") {
      When("pending")

      Then("pending")
    }
  }
}
