package com.github.mogproject.example.dynamodb

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling._

import scala.annotation.meta.beanGetter
import scala.beans.BeanProperty
import scala.collection.JavaConverters._

trait DynamoDBClient {
  private[this] val accessKeyId = sys.env("AWS_ACCESS_KEY")
  private[this] val secretAccessKey = sys.env("AWS_SECRET_KEY")
  private[this] val region = RegionUtils.getRegion("ap-northeast-1")
  private[this] val endpoint = region.getServiceEndpoint("dynamodb")

  private[this] val credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey)
  private[this] val client = {
    val ret = new AmazonDynamoDBClient(credentials)
    ret.setRegion(region)
    ret.setEndpoint(endpoint)
    ret
  }
  protected val mapper = new DynamoDBMapper(client)

  def batchSave(xs: FoodLog*) = mapper.batchSave(xs.asJava)

  def batchDelete(xs: FoodLog*) = mapper.batchDelete(xs.asJava)

  def batchWrite(toWrite: Seq[FoodLog], toDelete: Seq[FoodLog]) = mapper.batchWrite(toWrite.asJava, toDelete.asJava)
}

@DynamoDBTable(tableName = "FoodLog")
case class FoodLog(
                    @(DynamoDBHashKey@beanGetter)(attributeName = "UserId") @BeanProperty var userId: String,
                    @(DynamoDBRangeKey@beanGetter)(attributeName = "Timestamp") @BeanProperty var timestamp: Long,
                    @DynamoDBAttribute(attributeName = "Food") @BeanProperty var food: String,
                    @DynamoDBAttribute(attributeName = "Calorie") @BeanProperty var calorie: Int
                    ) {
  def this() = this(null, 0, null, 0)
}

object FoodLog extends DynamoDBClient {
  def readRecent(userId: String, limit: Int): Seq[FoodLog] = {
    val query = new DynamoDBQueryExpression[FoodLog]()
      .withHashKeyValues(FoodLog(userId, 0, null, 0))
      .withScanIndexForward(false)
      .withLimit(limit)
      .withConsistentRead(false)
    mapper.queryPage(classOf[FoodLog], query).getResults.asScala
  }

  def readAll(userId: String, limit: Int): Seq[FoodLog] = {
    val query = new DynamoDBQueryExpression[FoodLog]()
      .withHashKeyValues(FoodLog(userId, 0, null, 0))
      .withScanIndexForward(false)
      .withLimit(limit)
      .withConsistentRead(false)
    mapper.query(classOf[FoodLog], query).asScala
  }
}
