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

package uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.messages

import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.JsonFileHttpResponse

object JsonFileMessagesResponseProvider extends MessagesResponseProvider {
  override def invalidNino: HttpResponse = JsonFileHttpResponse(400, "account-not-found.json")
  override def missingVersionNumber: HttpResponse = JsonFileHttpResponse(400, "no-version.json")
  override def incorrectAuthorizationHeader: HttpResponse = JsonFileHttpResponse(401, "incorrect-authorization-header.json")
  override def missingSystemId: HttpResponse = JsonFileHttpResponse(400, "no-system-id.json")
  override def allMandatoryFieldsPopulated: HttpResponse = JsonFileHttpResponse(200, "messages-all-mandatory-fields.json")
  override def accountWithNoMessages: HttpResponse = JsonFileHttpResponse(200, "zero-messages.json")
  override def accountWithMultipleReadMessages: HttpResponse = JsonFileHttpResponse(200, "multiple-read-messages.json")
}
