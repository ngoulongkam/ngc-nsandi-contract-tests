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

import org.scalatest.exceptions.TestPendingException
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.Spreadsheet

object XlsxAccountResponseProvider extends AccountTestResponseProvider {

  override def incorrectAuthorizationHeader: HttpResponse = Spreadsheet.response("Account-IncorrectAuthorizationHeader")

  override def nullAuthorizationHeader: HttpResponse = Spreadsheet.response("Account-Null authorization header")

  override def noVersion: HttpResponse = Spreadsheet.response("Account-NoVersion")

  override def invalidVersion: HttpResponse = Spreadsheet.response("Account-InvalidVersionID")

  override def noNino: HttpResponse = Spreadsheet.response("Account-NoNino")

  override def invalidNino: HttpResponse = Spreadsheet.response("Account-NINOWithSPACING")

  override def invalidParams: HttpResponse = Spreadsheet.response("Account-InvalidParams")

  override def accountNotFound: HttpResponse = Spreadsheet.response("Account-Invalid NINO")

  override def noSystemId: HttpResponse = Spreadsheet.response("Account-NoSystemId")

  override def noSystemIdNinoOrVersion: HttpResponse = Spreadsheet.response("Account-Empty String")

  override def allFieldsPopulated: HttpResponse = Spreadsheet.response("Account -Check all fields present in Response")

  override def allMandatoryFieldsPopulated: HttpResponse = Spreadsheet.response("Account - Check all mandatory fields populated in Response")

  override def closedAccount: HttpResponse = Spreadsheet.response("Account-ClosedAccount")

  override def blockedAccount: HttpResponse = Spreadsheet.response("Account-BlockedAccount")

  override def termNumbersFieldPopulated: HttpResponse = Spreadsheet.response("Account -Check Term1 and Term customer and check Term2 Number set correctly")

  override def noBankDetailsAccount: HttpResponse = Spreadsheet.response("Account- Account Opened No bank details")

  override def accountWithBalance: HttpResponse = Spreadsheet.response("Account - Customer with Non zero balance")

  override def accountWithCurrentInvestmentMonth: HttpResponse = Spreadsheet.response("Account - Customer with non zero amount paid in current month")

  override def accountWithZeroBalanceAndBonus: HttpResponse = Spreadsheet.response("Account-Customer who has zero balance and zero bonus")

  override def accountWithUKPostcode: HttpResponse = Spreadsheet.response("Account -Check Account With UK postcode")

  override def accountWithBuildingSocietyBankDetails: HttpResponse = Spreadsheet.response("Account - Check Account With Building society as bank details")

  override def accountPaidInMaxForTheMonth: HttpResponse = Spreadsheet.response("Account - Customer with no headroomAccount-MaxPaidInForTheMonth")

  override def accountWithZeroBalance: HttpResponse = Spreadsheet.response("Account - Customer with zero balance")

  override def accountWithNoCorrelationId: HttpResponse = Spreadsheet.response("Account-NoCorrelationID")

  override def accountWithChannelIslandsPostcode: HttpResponse = Spreadsheet.response("Account -Check Account With Channel Island postcode")

  override def accountWithIsleOfManPostcode: HttpResponse = Spreadsheet.response("Account -Check Account With IOM postcode")

  override def accountWith1stTermBonusNotYetBeenPaid: HttpResponse = Spreadsheet.response("Account-Customer who has estimated 1st term bonus greater than zero but bonus not yet paid")

  override def accountWith2ndTermBonusNotYetBeenPaid: HttpResponse = Spreadsheet.response("Account-Customer who has estimated 2nd  term bonus greater than zero but bonus not yet paid")

  override def accountWith1stTermBonusPaid: HttpResponse = Spreadsheet.response("Account-Customer who has estimated 1st  term bonus greater than zero and paid")

  override def accountWithMaxFirstTerm: HttpResponse = Spreadsheet.response("Account-Account-Retrieve-MaxFirstBonusReached")

  def pending = throw new TestPendingException

}
