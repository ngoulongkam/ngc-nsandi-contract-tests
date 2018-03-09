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

package uk.gov.hmrc.ngchelptosavecontract.mobilehelptosave

import java.net.URL

import com.netaporter.uri.dsl._
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.Play
import play.api.libs.json.JsValue
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{CoreGet, HeaderCarrier}
import uk.gov.hmrc.ngchelptosavecontract.scalatest.WSClientSpec
import uk.gov.hmrc.ngchelptosavecontract.support.{ServicesConfig, TestWSHttp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
  * Tests that check that NS&I's /nsi-services/account endpoint
  * meets the contract that mobile-help-to-save expects of it
  */
class AccountSpec extends AsyncWordSpec with Matchers with FutureAwaits with DefaultAwaitTimeout with WSClientSpec {
  private val servicesConfig = new ServicesConfig
  private val http: CoreGet = new TestWSHttp(wsClient)

  private val helpToSaveProxyBaseUrl: URL = new URL(servicesConfig.baseUrl("help-to-save-proxy"))
  private val accountUrl = new URL(helpToSaveProxyBaseUrl, "/help-to-save-proxy/nsi-services/account")
  private val accountApiVersion = "V1.0"

  private val isoDateRegex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"

  private implicit val hc: HeaderCarrier = HeaderCarrier()

  "GET /nsi-services/account" should {
    """Return 200 and account details when account is present for passed NINO""" in {
      account(Nino("EM000001A")).map { jsonBody =>
        (jsonBody \ "accountBalance").as[String] shouldBe "200.00"

        (jsonBody \ "currentInvestmentMonth" \ "investmentRemaining").as[String] shouldBe "0.00"
        (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String] shouldBe "50.00"
        (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String] should fullyMatch regex isoDateRegex

        ((jsonBody \ "terms")(0) \ "termNumber").as[String] shouldBe "1"
        ((jsonBody \ "terms")(0) \ "endDate").as[String] should fullyMatch regex isoDateRegex
        ((jsonBody \ "terms")(0) \ "bonusEstimate").as[String] shouldBe "100.00"

        ((jsonBody \ "terms")(1) \ "termNumber").as[String] shouldBe "2"
        ((jsonBody \ "terms")(1) \ "endDate").as[String] should fullyMatch regex isoDateRegex
        ((jsonBody \ "terms")(1) \ "bonusEstimate").as[String] shouldBe "0.00"
      }
    }
  }

  private def account(nino: Nino, version: String = accountApiVersion, systemId: String="MDTP-mobile-contract-tests", correlationId: Option[String] = None)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[JsValue] =
    accountNoNinoValidation(nino.value, version, systemId, correlationId)

  //TODO make systemId shorter if ATOS don't want to increase maxlength from 20
  private def accountNoNinoValidation(nino: String, version: String = accountApiVersion, systemId: String="MDTP-mobile-contract-tests", correlationId: Option[String] = None)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[JsValue] =
    http.GET[JsValue](accountUrl.toString ? ("nino" -> nino) & ("version" -> accountApiVersion) & ("systemId" -> systemId) & ("correlationId" -> correlationId))

  // Workaround to prevent ClassCastException being thrown when setting up Play.xercesSaxParserFactory when "test" is run twice in the same sbt session.
  Play
}
