package org.jembi.bsis.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateDeserialiser extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    try {
      return CustomDateFormatter.getDateFromString(jp.getText());
    } catch (ParseException e) {
      throw new IllegalArgumentException();
    }
  }

}
