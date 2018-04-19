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
import uk.gov.hmrc.ngchelptosavecontract.support.AirGapSpreadsheet

object XlsxAccountResponseProvider extends AccountResponseProvider {

  override def incorrectAuthorizationHeader: HttpResponse = AirGapSpreadsheet.response("Account-IncorrectAuthorizationHeader")

  override def nullAuthorizationHeader: HttpResponse = AirGapSpreadsheet.response("Account-Null authorization header")

  override def noVersion: HttpResponse = AirGapSpreadsheet.response("Account-NoVersion")

  override def invalidVersion: HttpResponse = AirGapSpreadsheet.response("Account-InvalidVersionID")

  override def noNino: HttpResponse = AirGapSpreadsheet.response("Account-NoNino")

  override def invalidNino: HttpResponse = AirGapSpreadsheet.response("Account-NINOWithSPACING")

  override def invalidParams: HttpResponse = AirGapSpreadsheet.response("Account-InvalidParams")

  override def accountNotFound: HttpResponse = AirGapSpreadsheet.response("Account-Invalid NINO")

  override def noSystemId: HttpResponse = AirGapSpreadsheet.response("Account-NoSystemId")

  override def noSystemIdNinoOrVersion: HttpResponse = AirGapSpreadsheet.response("Account-Empty String")

  override def allFieldsPopulated: HttpResponse = AirGapSpreadsheet.response("Account -Check all fields present in Response")

  override def allMandatoryFieldsPopulated: HttpResponse = AirGapSpreadsheet.response("Account - Check all mandatory fields populated in Response")

  override def closedAccount: HttpResponse = AirGapSpreadsheet.response("Account-ClosedAccount")

  override def blockedAccount: HttpResponse = AirGapSpreadsheet.response("Account-BlockedAccount")

  override def termNumbersFieldPopulated: HttpResponse = AirGapSpreadsheet.response("Account -Check Term1 and Term customer and check Term2 Number set correctly")

  override def noBankDetailsAccount: HttpResponse = AirGapSpreadsheet.response("Account- Account Opened No bank details")

  override def accountWithBalance: HttpResponse = AirGapSpreadsheet.response("Account - Customer with Non zero balance")

  override def accountWithCurrentInvestmentMonth: HttpResponse = AirGapSpreadsheet.response("Account - Customer with non zero amount paid in current month")

  override def accountWithZeroBalanceAndBonus: HttpResponse = AirGapSpreadsheet.response("Account-Customer who has zero balance and zero bonus")

  override def accountWithUKPostcode: HttpResponse = AirGapSpreadsheet.response("Account -Check Account With UK postcode")

  override def accountWithBuildingSocietyBankDetails: HttpResponse = AirGapSpreadsheet.response("Account - Check Account With Building society as bank details")

  override def accountPaidInMaxForTheMonth: HttpResponse = AirGapSpreadsheet.response("Account - Customer with no headroomAccount-MaxPaidInForTheMonth")

  override def accountWithZeroBalance: HttpResponse = AirGapSpreadsheet.response("Account - Customer with zero balance")

  override def accountWithNoCorrelationId: HttpResponse = AirGapSpreadsheet.response("Account-NoCorrelationID")

  override def accountWithChannelIslandsPostcode: HttpResponse = AirGapSpreadsheet.response("Account -Check Account With Channel Island postcode")

  override def accountWithIsleOfManPostcode: HttpResponse = AirGapSpreadsheet.response("Account -Check Account With IOM postcode")

  override def accountWith1stTermBonusNotYetBeenPaid: HttpResponse = AirGapSpreadsheet.response("Account-Customer who has estimated 1st term bonus greater than zero but bonus not yet paid")

  override def accountWith2ndTermBonusNotYetBeenPaid: HttpResponse = AirGapSpreadsheet.response("Account-Customer who has estimated 2nd  term bonus greater than zero but bonus not yet paid")

  override def accountWith1stTermBonusPaid: HttpResponse = AirGapSpreadsheet.response("Account-Customer who has estimated 1st  term bonus greater than zero and paid")

  override def accountWithMaxFirstTerm: HttpResponse = AirGapSpreadsheet.response("Account-Account-Retrieve-MaxFirstBonusReached")

  def pending = throw new TestPendingException

}
