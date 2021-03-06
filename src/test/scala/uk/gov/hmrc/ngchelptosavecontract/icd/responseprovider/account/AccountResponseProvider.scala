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

trait AccountResponseProvider {
  def incorrectAuthorizationHeader: HttpResponse
  def nullAuthorizationHeader: HttpResponse
  def noVersion: HttpResponse
  def invalidVersion: HttpResponse
  def noNino: HttpResponse
  def invalidNino: HttpResponse
  def invalidParams: HttpResponse
  def accountNotFound: HttpResponse
  def noSystemId: HttpResponse
  def noSystemIdNinoOrVersion: HttpResponse
  def allFieldsPopulated: HttpResponse
  def allMandatoryFieldsPopulated: HttpResponse
  def closedAccount: HttpResponse
  def blockedAccount: HttpResponse
  def termNumbersFieldPopulated: HttpResponse
  def noBankDetailsAccount: HttpResponse
  def accountWithBalance: HttpResponse
  def accountWithCurrentInvestmentMonth: HttpResponse
  def accountWithZeroBalanceAndBonus: HttpResponse
  def accountWithUKPostcode: HttpResponse
  def accountWithBuildingSocietyBankDetails: HttpResponse
  def accountPaidInMaxForTheMonth: HttpResponse
  def accountWithZeroBalance: HttpResponse
  def accountWithNoCorrelationId: HttpResponse
  def accountWithChannelIslandsPostcode: HttpResponse
  def accountWithIsleOfManPostcode: HttpResponse
  def accountWith1stTermBonusNotYetBeenPaid: HttpResponse
  def accountWith2ndTermBonusNotYetBeenPaid: HttpResponse
  def accountWith1stTermBonusPaid: HttpResponse
  def accountWithMaxFirstTerm: HttpResponse
}
