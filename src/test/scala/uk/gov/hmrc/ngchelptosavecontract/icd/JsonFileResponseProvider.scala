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

package uk.gov.hmrc.ngchelptosavecontract.icd

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.HttpResponse

// We will probably delete this and use XlsxAccountResponseProvider instead
object JsonFileResponseProvider extends TestResponseProvider {
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

  private object JsonFileHttpResponse {
    def apply(status: Int, jsonLeafname: String): HttpResponse =
      HttpResponse(status, Some(loadJson(jsonLeafname)))

    private def loadJson(leafname: String): JsValue = {
      val inputStream = getClass.getResourceAsStream(s"/airgap/demo/$leafname")
      try {
        Json.parse(inputStream)
      }
      finally {
        if (inputStream != null) inputStream.close()
      }
    }
  }
}
