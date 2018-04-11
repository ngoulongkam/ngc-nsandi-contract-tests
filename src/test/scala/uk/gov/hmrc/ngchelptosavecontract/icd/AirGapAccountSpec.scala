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

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.HttpResponse

/**
  * Tests that check that the air gap JSON files for NS&I's
  * /nsi-services/account endpoint meet the full contract defined in the ICD
  * (even the parts that we do not currently rely on).
  *
  * We can only check the body, not the HTTP status code or headers, because the
  * JSON files contain only the body.
  */
class AirGapAccountSpec extends WordSpec with Matchers with AccountChecks {

  "GET account air gap JSON" should {

    "include all fields when a value is available" in {
      checkAllFieldsPresentResponse(JsonFileHttpResponse(200, "all-fields.json"))
    }

    "return 400 HTS-API015-002 when no version parameter is passed" in {
      checkNoVersionResponse(JsonFileHttpResponse(400, "no-version.json"))
    }

    "return 400 HTS-API015-003 when an incorrect version parameter is passed" in {
      checkInvalidVersionResponse(JsonFileHttpResponse(400, "invalid-version.json"))
    }

    "return 400 HTS-API015-004 when no NINO is passed" in {
      checkNoNinoResponse(JsonFileHttpResponse(400, "no-nino.json"))
    }

    "return 400 HTS-API015-005 when NINO with invalid format is passed" in {
      checkInvalidNinoResponse(JsonFileHttpResponse(400, "invalid-nino.json"))
    }

    "return 400 HTS-API015-006 when NINO without a Help to Save account is passed" in {
      checkNoAccountResponse(JsonFileHttpResponse(400, "no-account.json"))
    }

    "return 400 HTS-API015-012 when no systemId is passed" in {
      checkNoSystemIdResponse(JsonFileHttpResponse(400, "no-system-id.json"))
    }

    "return 400 with two error responses when a NINO with an invalid format and the incorrect version is passed" in {
      checkInvalidNinoAndVersionResponse(JsonFileHttpResponse(400, "invalid-nino-and-version.json"))
    }
  }

}

private object JsonFileHttpResponse {
  def apply(status: Int, jsonLeafname: String): HttpResponse =
    HttpResponse(status, Some(loadJson(jsonLeafname)))

  private def loadJson(leafname: String): JsValue = {
    val inputStream = getClass.getResourceAsStream(s"/airgap/$leafname")
    try {
      Json.parse(inputStream)
    }
    finally {
      if (inputStream != null) inputStream.close()
    }
  }
}
