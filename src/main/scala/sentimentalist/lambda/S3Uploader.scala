package sentimentalist.lambda

import io.circe.generic.auto._
import io.circe.syntax._
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{PutObjectRequest, PutObjectResponse}

object S3Uploader {

  def upload(
      s3: S3Client,
      bucket: String
  )(key: String, analysedCases: Seq[AnalysedCase]): PutObjectResponse = {
    val request = PutObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .contentType("application/json")
      .build()
    s3.putObject(request, RequestBody.fromString(analysedCases.asJson.noSpaces))
  }
}
