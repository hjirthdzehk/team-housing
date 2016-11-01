package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class MeterSpec extends Specification {

  "Meter" should {

    

    "find by primary keys" in new AutoRollback {
      val maybeFound = Meter.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Meter.findBy(sqls"meter_id = ${123}")
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Meter.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Meter.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Meter.findAllBy(sqls"meter_id = ${123}")
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Meter.countBy(sqls"meter_id = ${123}")
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Meter.create(meterUnitId = 123, flatId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Meter.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Meter.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Meter.findAll().head
      Meter.destroy(entity)
      val shouldBeNone = Meter.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Meter.findAll()
      entities.foreach(e => Meter.destroy(e))
      val batchInserted = Meter.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
