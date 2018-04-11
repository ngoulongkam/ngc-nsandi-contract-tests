package uk.gov.hmrc.ngchelptosavecontract.support

import com.fasterxml.jackson.databind.JsonNode
import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.ngchelptosavecontract.support.JsonSchemaValidatorImp.{loadResource, validateExample}

class JsonSchemaValidatorSpec extends WordSpec with Matchers {

  val jsonNodeSchema: JsonNode = loadResource("/uk/gov/hmrc/ngchelptosavecontract/support/get_account_by_nino_RESP_schema_V1.0.json")
  val jsonNodeExample: JsonNode = loadResource("/uk/gov/hmrc/ngchelptosavecontract/support/get_account_by_nino_RESP_example_V1.0.json")

  "validateExample" should {
    "return true when the given json example matches the given schema" in {
      val result = validateExample(jsonNodeSchema, jsonNodeExample)
      result.isRight
    }

    "return false when the given json example does not match the given schema" in {
      val badJsonNodeExample: JsonNode = loadResource("/uk/gov/hmrc/ngchelptosavecontract/support/badJsonExample.json")
      val result = validateExample(jsonNodeSchema, badJsonNodeExample)
      result.isLeft
    }
  }
}
