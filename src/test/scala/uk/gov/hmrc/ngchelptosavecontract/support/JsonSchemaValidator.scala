package uk.gov.hmrc.ngchelptosavecontract.support

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.Logger

class JsonSchemaValidator {

  def validateExample(givenSchema: JsonNode, givenExample: JsonNode): Either[String, Unit] = {
    val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
    val schema: JsonSchema = factory.getJsonSchema(givenSchema)

    val processingReport = schema.validate(givenExample)

    processingReport.isSuccess match {
      case true => Right(())
      case false =>
        Logger.info(s"Processing report: ${processingReport.iterator().next()}")
        Left(s"The given json example does not match the schema, ${processingReport.iterator().next()}")
    }
  }

  def loadResource(name: String): JsonNode = JsonLoader.fromResource(name)
}

object JsonSchemaValidatorImp extends JsonSchemaValidator {

}

