package uk.gov.hmrc.ngchelptosavecontract.support

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}

class JsonSchemaValidator {

  def validateExample(givenSchema: JsonNode, givenExample: JsonNode): Either[String, Unit] = {
    val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
    val schema: JsonSchema = factory.getJsonSchema(givenSchema)

    val processingReport = schema.validate(givenExample)

    processingReport.isSuccess match {
      case true => Right(())
      case false => Left("The given json example does not match the schema")
    }
  }

  def loadResource(name: String): JsonNode = JsonLoader.fromResource(name)
}

object JsonSchemaValidatorImp extends JsonSchemaValidator {

}

