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

import io.lemonlabs.uri.dsl._
import org.scalatest.{Assertion, AsyncWordSpec, Matchers}
import play.api.Play
import play.api.libs.json.{JsObject, JsValue}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{CoreGet, HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.ngchelptosavecontract.icd.AccountChecks
import uk.gov.hmrc.ngchelptosavecontract.scalatest.WSClientSpec
import uk.gov.hmrc.ngchelptosavecontract.support.ScalaUriConfig.config
import uk.gov.hmrc.ngchelptosavecontract.support.{ServicesConfig, TestWSHttp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
  * Tests that check that NS&I's /nsi-services/account endpoint
  * meets the contract that mobile-help-to-save expects of it
  */
class AccountSpec
  extends AsyncWordSpec with Matchers
    with FutureAwaits with DefaultAwaitTimeout with WSClientSpec
    with AccountChecks {
  private val servicesConfig = new ServicesConfig
  private val http: CoreGet = new TestWSHttp(wsClient)

  private val helpToSaveProxyBaseUrl: URL = new URL(servicesConfig.baseUrl("help-to-save-proxy"))
  private val accountUrl = new URL(helpToSaveProxyBaseUrl, "/help-to-save-proxy/nsi-services/account")
  private val accountApiVersion = "V1.0"

  private val isoDateRegex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"

  private val ninoWithHtsAccount = Nino("EM000001A")
  private val ninoWithoutHtsAccount = Nino("EM123456A")

  private implicit val hc: HeaderCarrier = HeaderCarrier()

  "GET /nsi-services/account" should {
    """Return 200 and account details when account is present for passed NINO""" in {
      account(ninoWithHtsAccount).map { jsonBody =>
        (jsonBody \ "accountBalance").as[String] shouldBe "200.00"

        (jsonBody \ "currentInvestmentMonth" \ "investmentRemaining").as[String] shouldBe "0.00"
        (jsonBody \ "currentInvestmentMonth" \ "investmentLimit").as[String] shouldBe "50.00"
        (jsonBody \ "currentInvestmentMonth" \ "endDate").as[String] should fullyMatch regex isoDateRegex

        ((jsonBody \ "terms")(0) \ "termNumber").as[Int] shouldBe 1
        ((jsonBody \ "terms")(0) \ "endDate").as[String] should fullyMatch regex isoDateRegex
        ((jsonBody \ "terms")(0) \ "bonusEstimate").as[String] shouldBe "100.00"

        ((jsonBody \ "terms")(1) \ "termNumber").as[Int] shouldBe 2
        ((jsonBody \ "terms")(1) \ "endDate").as[String] should fullyMatch regex isoDateRegex
        ((jsonBody \ "terms")(1) \ "bonusEstimate").as[String] shouldBe "0.00"

        withClue(jsonBody \ "correlationId") {
          jsonBody.as[JsObject].keys should not contain "correlationId"
        }
      }
    }

    """Include correlationId in success response when correlationId query parameter passed""" in {
      def correlationIdShouldBeReturned(passedCorrelationId: String): Future[Assertion] = {
        account(ninoWithHtsAccount, correlationId = Some(passedCorrelationId)).map { jsonBody =>
          jsonBody.as[JsObject].keys should contain("correlationId")
          (jsonBody \ "correlationId").as[String] shouldBe passedCorrelationId

          // other fields should also be present
          (jsonBody \ "accountBalance").as[String] shouldBe "200.00"
        }
      }

      for {
        _ <- correlationIdShouldBeReturned("easy")
        _ <- correlationIdShouldBeReturned("some&nasty+chars <>")
      } yield succeed
    }

    """Include correlationId in error response when correlationId query parameter passed""" in {
      implicit val responseHandler: HttpReads[HttpResponse] = new HttpReads[HttpResponse] {
        override def read(method: String, url: String, response: HttpResponse): HttpResponse = response
      }

      val passedCorrelationId = "some&nasty+chars <>"

      http.GET[HttpResponse](accountUrlWithParams(Some(ninoWithHtsAccount), version = None, correlationId = Some(passedCorrelationId))).map { response =>
        response.status shouldBe 400
        val jsonBody = response.json
        jsonBody.as[JsObject].keys should contain("errors")
        (jsonBody \ "correlationId").as[String] shouldBe passedCorrelationId
      }
    }

    "return 400 HTS-API015-002 when no version parameter is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParams(Some(ninoWithHtsAccount), version = None))
        .map(checkNoVersionResponse)
    }

    "return 400 HTS-API015-003 when an incorrect version parameter is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParams(Some(ninoWithHtsAccount), version = Some("V0.0")))
        .map(checkInvalidVersionResponse)
    }

    "return 400 HTS-API015-004 when no NINO is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParams(nino = None))
        .map(checkNoNinoResponse)
    }

    "return 400 HTS-API015-005 when NINO with invalid format is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParamsUnvalidatedNino(nino = Some("not a NINO")))
        .map(checkInvalidNinoResponse)
    }

    "return 400 HTS-API015-006 when NINO without a Help to Save account is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParams(nino = Some(ninoWithoutHtsAccount)))
        .map(checkNoAccountResponse)
    }

    "return 400 HTS-API015-012 when no systemId is passed" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParams(Some(ninoWithHtsAccount), systemId = None))
        .map(checkNoSystemIdResponse)
    }

    "return 400 with multiple errors when there are multiple request errors" in {
      implicit val httpReads: HttpReads[HttpResponse] = NoErrorHandling.httpReads

      http.GET[HttpResponse](accountUrlWithParamsUnvalidatedNino(nino = Some("not a NINO"), version = Some("V0.0")))
        .map(checkInvalidNinoAndVersionResponse)

      http.GET[HttpResponse](accountUrlWithParams(nino = None, version = None, systemId = None, correlationId = None))
        .map(checkNoSystemIdNinoOrVersionResponse)
    }
  }

  "this test's accountUrlWithParams method" should {
    "omit parameters when they are None" in {
      accountUrlWithParams(Some(ninoWithHtsAccount)) should include ("nino")
      accountUrlWithParams(None) should not include "nino"
      accountUrlWithParams(Some(ninoWithHtsAccount), correlationId = Some("something")) should include("correlationId")
      accountUrlWithParams(Some(ninoWithHtsAccount)) should not include "correlationId"
    }
  }

  private val defaultSystemId = "MDTP-MOBILE"

  private def account(nino: Nino, version: String = accountApiVersion, systemId: String = defaultSystemId, correlationId: Option[String] = None)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[JsValue] =
    http.GET[JsValue](accountUrlWithParams(Some(nino), Some(version), Some(systemId), correlationId))

  private def accountUrlWithParams(nino: Option[Nino], version: Option[String] = Some(accountApiVersion), systemId: Option[String] = Some(defaultSystemId), correlationId: Option[String] = None): String =
    accountUrlWithParamsUnvalidatedNino(nino.map(_.value), version, systemId, correlationId)

  private def accountUrlWithParamsUnvalidatedNino(nino: Option[String], version: Option[String] = Some(accountApiVersion), systemId: Option[String] = Some(defaultSystemId), correlationId: Option[String] = None): String =
    accountUrl.toString ? ("nino" -> nino) & ("version" -> version) & ("systemId" -> systemId) & ("correlationId" -> correlationId)

  // Workaround to prevent ClassCastException being thrown when setting up Play.xercesSaxParserFactory when "test" is run twice in the same sbt session.
  Play
}

object NoErrorHandling {
  val httpReads: HttpReads[HttpResponse] = new HttpReads[HttpResponse] {
    override def read(method: String, url: String, response: HttpResponse): HttpResponse = response
  }
}
