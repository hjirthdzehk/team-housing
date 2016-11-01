package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class MeterUnitSpec extends Specification {

  "MeterUnit" should {

    

    "find by primary keys" in new AutoRollback {
      val maybeFound = MeterUnit.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = MeterUnit.findBy(sqls"meter_unit_id = ${123}")
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = MeterUnit.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = MeterUnit.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = MeterUnit.findAllBy(sqls"meter_unit_id = ${123}")
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = MeterUnit.countBy(sqls"meter_unit_id = ${123}")
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = MeterUnit.create()
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = MeterUnit.findAll().head
      // TODO modify something
      val modified = entity
      val updated = MeterUnit.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = MeterUnit.findAll().head
      MeterUnit.destroy(entity)
      val shouldBeNone = MeterUnit.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = MeterUnit.findAll()
      entities.foreach(e => MeterUnit.destroy(e))
      val batchInserted = MeterUnit.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
