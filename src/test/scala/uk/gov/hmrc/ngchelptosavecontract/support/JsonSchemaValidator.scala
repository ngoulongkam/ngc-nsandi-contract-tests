package uk.gov.hmrc.ngchelptosavecontract.support

import play.api.libs.json.Json
import play.libs.Json



import com.github.fge.jsonschema.core.report.ProcessingMessage

import scala.collection.JavaConversions._

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonschema.main.JsonSchemaFactory

import org.json4s._
import org.json4s.jackson.JsonMethods._



class JsonSchemaValidator {

//  def schema(schemaType: Any) = Json.fromJson[schemaType](Json.parse(
//    """{
//      |"properties": {
//      |  "id":    { "type": "integer" },
//      |  "title": { "type": "string" },
//      |  "body":  { "type": "string" }
//      |}
//    }""".stripMargin)).get
//
//  def schemaValidator(schemaType: Any, example: Json) = Json.fromJson[schemaType](
//   Json.toJson(example)
//  )


  def jsonSchemaValidator(schema: Json, example: Json): String = {
    val schema: JsonNode = asJsonNode(parse(jsonSchema))
    val instance: JsonNode = asJsonNode(parse(json))

    val validator = JsonSchemaFactory.byDefault().getValidator

    val processingReport = validator.validate(schema, instance)

    if (processingReport.isSuccess) {
      println("JSON Schema validation was successful")
    } else {
      processingReport.foreach { message: ProcessingMessage =>
        println(message.asJson())
      }
    }
  }
}
