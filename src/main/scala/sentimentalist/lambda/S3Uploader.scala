package sentimentalist.lambda

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{
  PutObjectRequest,
  PutObjectResponse
}

import scala.collection.JavaConverters._

object S3Uploader {

  private val metadata = Map.empty[String, String].asJava

  def upload(
      s3: S3Client,
      bucket: String
  )(key: String, content: String): PutObjectResponse = {
    val request = PutObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .metadata(metadata)
      .build()
    s3.putObject(request, RequestBody.fromString(content))
  }
}
