package com.github.mogproject.example.dynamodb

object TestSave extends App {
  import scala.util.Random
  val item1 = (1 to 10).map(i => FoodLog("user-1", Random.nextInt(100000), s"food-$i", Random.nextInt(2000)))
  val item2 = (1 to 10).map(i => FoodLog("user-2", Random.nextInt(100000), s"food-$i", Random.nextInt(2000)))
  val item3 = (1 to 100).map(i => FoodLog("user-3", Random.nextInt(100000), s"food-$i", Random.nextInt(2000)))
  FoodLog.batchSave(item1 ++ item2 ++ item3: _*)
}

object TestRead extends App {
  FoodLog.readRecent("user-1", 5) foreach println
}

object TestBench extends App {
  val s1 = System.currentTimeMillis()
  FoodLog.readAll("user-3", 5).size
  val e1 = System.currentTimeMillis()

  val s2 = System.currentTimeMillis()
  FoodLog.readRecent("user-3", 5).size
  val e2 = System.currentTimeMillis()

  val s3 = System.currentTimeMillis()
  FoodLog.readRecent("user-3", 100).size
  val e3 = System.currentTimeMillis()

  println(s"all, 5: ${e1 - s1}")
  println(s"recent, 5: ${e2 - s2}")
  println(s"recent, 100: ${e3 - s3}")
}