package models

import org.joda.time.DateTime

/** Created by a.kiselev on 30/10/2016. */
case class Meter(
  meterId: Int,
  installationDate: DateTime,
  `type`: String,
  title: String,
  meterUnitId: Int,
  active: Boolean,
  flatId: Int
) {}

