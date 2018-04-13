package uk.gov.hmrc.ngchelptosavecontract.support

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.ngchelptosavecontract.support.JsonSchemaValidatorImp.validateWithJsonSchema

class JsonSchemaValidatorSpec extends WordSpec with Matchers {

  val schema = "src/test/resources/json/get_account_by_nino_RESP_schema_V1.0.json"
  val goodJsonExample= "src/test/resources/json/get_account_by_nino_RESP_example_V1.0.json"
  val badJsonExample = "src/test/resources/json/badJsonExample.json"

  "validateExample" should {
    "return true when the given json example matches the given schema" in {
      val result = validateWithJsonSchema(schema, goodJsonExample)
      result.isRight
    }

    "return false when the given json example does not match the given schema" in {
      val result = validateWithJsonSchema(schema, badJsonExample)
      result.isLeft
    }
  }



  "validateWithJsonSchema" should {
    "return all error lines when the given json example doesn't match the schema" in {
      val result = validateWithJsonSchema(schema, badJsonExample)
      result.isLeft
      println(result)
    }

  }


}
