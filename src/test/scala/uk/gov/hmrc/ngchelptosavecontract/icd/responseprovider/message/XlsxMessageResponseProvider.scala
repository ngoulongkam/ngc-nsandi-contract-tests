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

package uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.message

import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.AirGapSpreadsheet

object XlsxMessageResponseProvider extends MessageResponseProvider{

  override def noMessageId: HttpResponse = AirGapSpreadsheet.response("Message-No-MessageID")

  override def missingVersionNumber: HttpResponse = AirGapSpreadsheet.response("Message-NoVersion")

  override def missingSystemId: HttpResponse = AirGapSpreadsheet.response("Message-NoSystemId")

  override def allMandatoryFieldsPopulated: HttpResponse = AirGapSpreadsheet.response("Message-CheckAllMandatoryField")
}
