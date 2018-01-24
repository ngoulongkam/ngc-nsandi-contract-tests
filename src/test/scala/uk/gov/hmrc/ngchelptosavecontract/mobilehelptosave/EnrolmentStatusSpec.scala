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

import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.Play
import play.api.libs.json.JsValue
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.domain.Generator
import uk.gov.hmrc.http.{CoreGet, HeaderCarrier, HttpResponse}
import uk.gov.hmrc.ngchelptosavecontract.scalatest.WSClientSpec
import uk.gov.hmrc.ngchelptosavecontract.support.authloginapi.AuthLoginApiConnector
import uk.gov.hmrc.ngchelptosavecontract.support.{ServicesConfig, TestWSHttp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
  * Tests that check that the /help-to-save/enrolment-status in help-to-save
  * meets the contract that mobile-help-to-save expects of it
  */
class EnrolmentStatusSpec extends AsyncWordSpec with Matchers with FutureAwaits with DefaultAwaitTimeout with WSClientSpec {
  private val servicesConfig = new ServicesConfig
  private val authLoginApiConnector = new AuthLoginApiConnector(servicesConfig, wsClient)
  private val http: CoreGet = new TestWSHttp(wsClient)
  private val generator = new Generator()

  private val helpToSaveBaseUrl: URL = new URL(servicesConfig.baseUrl("help-to-save"))
  private val enrolmentStatusUrl = new URL(helpToSaveBaseUrl, "/help-to-save/enrolment-status")
  private val enrolUserUrl = new URL(helpToSaveBaseUrl, "/help-to-save/enrol-user")

  "/help-to-save/enrolment-status" should {
    """include "enrolled": false when user is not enrolled in Help to Save""" in {
      authLoginApiConnector.governmentGatewayLogin(Some(generator.nextNino)).flatMap { implicit hc =>
        for {
          enrolled <- enrolmentStatus()
        } yield {
          enrolled shouldBe false
        }
      }
    }

    """include "enrolled": true when user is enrolled in Help to Save""" in {
      authLoginApiConnector.governmentGatewayLogin(Some(generator.nextNino)).flatMap { implicit hc =>
        for {
          _ <- enrolUser()
          enrolled <- enrolmentStatus()
        } yield {
          enrolled shouldBe true
        }
      }
    }
  }

  private def enrolUser()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Unit] =
    http.GET[HttpResponse](enrolUserUrl.toString).map { _ => () }

  private def enrolmentStatus()(implicit hc: HeaderCarrier, ec: ExecutionContext) =
    http.GET[JsValue](enrolmentStatusUrl.toString).map { json: JsValue =>
      (json \ "enrolled").as[Boolean]
    }

  // Workaround to prevent ClassCastException being thrown when setting up Play.xercesSaxParserFactory when "test" is run twice in the same sbt session.
  Play
}
