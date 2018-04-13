package uk.gov.hmrc.ngchelptosavecontract.support

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import uk.gov.hmrc.ngchelptosavecontract.support.JsErrorOps._

import scala.io.Source

class JsonSchemaValidator {

  private def loadJsonFromFile(filename: String): JsValue = {
    Json.parse(Source.fromFile(filename).getLines().mkString("\n"))
  }

  private def validationSchema(schemaFilePath: String): SchemaType = {
    Json.fromJson[SchemaType](loadJsonFromFile(schemaFilePath)).getOrElse(sys.error("Could not parse schema string"))
  }

  private lazy val jsonValidator: SchemaValidator = SchemaValidator()

  def validateWithJsonSchema(schemaFilePath: String, jsonFilePath: String): Either[String, JsValue] = {
    val schema = validationSchema(schemaFilePath)
    val jsonExample = loadJsonFromFile(jsonFilePath)
    jsonValidator.validate(schema, jsonExample) match {
      case e: JsError ⇒ Left(s"Given json was not valid against schema: ${e.prettyPrint()}")
      case JsSuccess(u, _) ⇒ Right(u)
    }
  }


}

object JsonSchemaValidatorImp extends JsonSchemaValidator {

}

