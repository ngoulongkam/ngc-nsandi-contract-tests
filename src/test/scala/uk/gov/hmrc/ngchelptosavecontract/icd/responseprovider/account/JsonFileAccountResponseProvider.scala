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

package uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.account

import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.JsonFileHttpResponse

// We will probably delete this and use XlsxAccountResponseProvider instead
object JsonFileAccountResponseProvider extends AccountResponseProvider {
  override def incorrectAuthorizationHeader: HttpResponse = JsonFileHttpResponse(401, "incorrect-authorization-header.json")
  override def nullAuthorizationHeader: HttpResponse = JsonFileHttpResponse(401, "null-authorization-header.json")
  override def noVersion: HttpResponse = JsonFileHttpResponse(400, "no-version.json")
  override def invalidVersion: HttpResponse = JsonFileHttpResponse(400, "invalid-version.json")
  override def noNino: HttpResponse = JsonFileHttpResponse(400, "no-nino.json")
  override def invalidNino: HttpResponse = JsonFileHttpResponse(400, "invalid-nino.json")
  override def invalidParams: HttpResponse = JsonFileHttpResponse(400, "invalid-params.json")
  override def accountNotFound: HttpResponse = JsonFileHttpResponse(400, "account-not-found.json")
  override def noSystemId: HttpResponse = JsonFileHttpResponse(400, "no-system-id.json")
  override def noSystemIdNinoOrVersion: HttpResponse = JsonFileHttpResponse(400, "no-system-id-nino-or-version.json")
  override def allFieldsPopulated: HttpResponse = JsonFileHttpResponse(200, "all-fields.json")
  override def allMandatoryFieldsPopulated: HttpResponse = JsonFileHttpResponse(200, "all-mandatory-fields.json")
  override def closedAccount: HttpResponse = JsonFileHttpResponse(200, "closed-account.json")
  override def blockedAccount: HttpResponse = JsonFileHttpResponse(200, "blocked-account.json")
  override def termNumbersFieldPopulated: HttpResponse = JsonFileHttpResponse(200, "term-number.json")
  override def noBankDetailsAccount: HttpResponse = JsonFileHttpResponse(200, "no-bank-details.json")
  override def accountWithBalance: HttpResponse = JsonFileHttpResponse(200, "non-zero-balance-account.json")
  override def accountWithCurrentInvestmentMonth: HttpResponse = JsonFileHttpResponse(200, "non-zero-amount-paid-this-month.json")
  override def accountWithZeroBalanceAndBonus: HttpResponse = JsonFileHttpResponse(200, "zero-balance-account.json")
  override def accountWithUKPostcode: HttpResponse = JsonFileHttpResponse(200, "uk-postcode-account.json")
  override def accountWithBuildingSocietyBankDetails: HttpResponse = JsonFileHttpResponse(200, "building-society-bank-details.json")
  override def accountPaidInMaxForTheMonth: HttpResponse = JsonFileHttpResponse(200, "no-headroom-account.json")
  override def accountWithZeroBalance: HttpResponse = JsonFileHttpResponse(200, "zero-balance-account.json")
  override def accountWithNoCorrelationId: HttpResponse = JsonFileHttpResponse(200, "generate-new-correlation-id.json")
  override def accountWithChannelIslandsPostcode: HttpResponse = JsonFileHttpResponse(200, "channel-islands-postcode.json")
  override def accountWithIsleOfManPostcode: HttpResponse = JsonFileHttpResponse(200, "isle-of-man-postcode.json")
  override def accountWith1stTermBonusNotYetBeenPaid: HttpResponse = JsonFileHttpResponse(200, "account-with-1st-term-bonus-not-been-paid.json")
  override def accountWith2ndTermBonusNotYetBeenPaid: HttpResponse = JsonFileHttpResponse(200, "account-with-2nd-term-bonus-not-been-paid.json")
  override def accountWith1stTermBonusPaid: HttpResponse = JsonFileHttpResponse(200, "account-with-1st-term-bonus-paid.json")
  override def accountWithMaxFirstTerm: HttpResponse = JsonFileHttpResponse(200, "account-with-maximum-1st-term-bonus.json")
}
