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

package uk.gov.hmrc.ngchelptosavecontract.support

import com.typesafe.config.ConfigFactory
import play.api.Configuration

/**
  * Cut down version of play-config's uk.gov.hmrc.play.config.ServicesConfig
  * without run mode support as it makes less sense outside a microservice.
  */
class ServicesConfig {

  protected def configuration: Configuration = Configuration(
    ConfigFactory.load(environment)
  )

  private lazy val environment = {
    sys.props.get("environment").getOrElse("local")
  }

  protected lazy val rootServices = "microservice.services"

  protected lazy val defaultProtocol: String =
    configuration.getString(s"$rootServices.protocol")
      .getOrElse("http")

  def baseUrl(serviceName: String): String = {
    val protocol = getConfString(s"$serviceName.protocol",defaultProtocol)
    val host = getConfString(s"$serviceName.host", throw new RuntimeException(s"Could not find config $serviceName.host"))
    val port = getConfInt(s"$serviceName.port", throw new RuntimeException(s"Could not find config $serviceName.port"))
    s"$protocol://$host:$port"
  }

  def getConfString(confKey: String, defString: => String): String = {
    configuration.getString(s"$rootServices.$confKey").
      getOrElse(defString)
  }

  def getConfInt(confKey: String, defInt: => Int): Int = {
    configuration.getInt(s"$rootServices.$confKey").
      getOrElse(defInt)
  }

}
