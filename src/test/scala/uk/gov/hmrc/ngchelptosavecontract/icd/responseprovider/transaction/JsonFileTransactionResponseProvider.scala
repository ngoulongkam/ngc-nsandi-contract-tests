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

package uk.gov.hmrc.ngchelptosavecontract.icd.responseprovider.transaction

import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.ngchelptosavecontract.support.JsonFileHttpResponse

// We will probably delete this and use XlsxTransactionResponseProvider instead
object JsonFileTransactionResponseProvider extends TransactionResponseProvider {
  override def invalidNino: HttpResponse = JsonFileHttpResponse(400, "account-not-found.json")
  override def missingVersionNumber: HttpResponse = JsonFileHttpResponse(400, "no-version.json")
  override def incorrectAuthorizationHeader: HttpResponse = JsonFileHttpResponse(401, "incorrect-authorization-header.json")
  override def missingSystemId: HttpResponse = JsonFileHttpResponse(400, "no-system-id.json")
  override def allMandatoryFieldsPopulated: HttpResponse = JsonFileHttpResponse(200, "transaction-all-mandatory-fields.json")
  override def accountWithNoTransaction: HttpResponse = JsonFileHttpResponse(200, "zero-transactions.json")
}
