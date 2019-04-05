package sentimentalist.lambda

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{
  PutObjectRequest,
  PutObjectResponse
}

object S3Uploader {

  def upload(
      s3: S3Client,
      bucket: String
  )(key: String, content: String): PutObjectResponse = {
    val request = PutObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .contentType("application/json")
      .build()
    s3.putObject(request, RequestBody.fromString(content))
  }
}
