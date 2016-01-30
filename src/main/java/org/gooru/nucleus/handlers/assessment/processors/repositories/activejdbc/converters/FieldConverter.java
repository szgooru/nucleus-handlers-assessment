package org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.converters;

import org.postgresql.util.PGobject;

import java.sql.SQLException;

/**
 * Created by ashish on 28/1/16.
 */
public interface FieldConverter {
  PGobject convertField(Object fieldValue);

  static PGobject convertFieldToJson(String value) {
    String JSONB_TYPE = "jsonb";
    PGobject pgObject = new PGobject();
    pgObject.setType(JSONB_TYPE);
    try {
      pgObject.setValue(value);
      return pgObject;
    } catch (SQLException e) {
      return null;
    }
  }

  static PGobject convertFieldToUuid(String value) {
    String UUID_TYPE = "uuid";
    PGobject pgObject = new PGobject();
    pgObject.setType(UUID_TYPE);
    try {
      pgObject.setValue(value);
      return pgObject;
    } catch (SQLException e) {
      return null;
    }
  }
}
