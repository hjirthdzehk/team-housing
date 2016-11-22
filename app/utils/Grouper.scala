package utils

/** Created by a.kiselev on 22/11/2016. */
object Grouper {

  def groupToMap[K, V](seq: Seq[(K, V)]): Map[K, Seq[V]] = seq
    .groupBy(_._1)
    .mapValues(_.map(_._2))

}
