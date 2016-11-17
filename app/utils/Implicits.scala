package utils

/** Created by a.kiselev on 16/11/2016. */
object Implicits {
  implicit def toStringOpt(s: String): Option[String] = Some(s)

  implicit def fromStringOpt(sOpt: Option[String]): String = sOpt.getOrElse("")
}
