package sentimentalist.lambda

import software.amazon.awssdk.services.comprehend.model.BatchDetectEntitiesItemResult

import scala.collection.JavaConverters._

case class Entity(
    score: Double,
    entityType: String,
    text: String,
    beginOffset: Int,
    endOffset: Int
)

object Entity {

  def fromAnalysis(entityAnalysis: BatchDetectEntitiesItemResult): Seq[Entity] = {
    entityAnalysis.entities.asScala map { entity =>
      Entity(
        score = entity.score().toDouble,
        entityType = entity.typeAsString(),
        text = entity.text(),
        beginOffset = entity.beginOffset(),
        endOffset = entity.endOffset()
      )
    }
  }
}
