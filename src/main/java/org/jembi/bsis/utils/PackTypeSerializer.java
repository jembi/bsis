package org.jembi.bsis.utils;

import java.io.IOException;

import org.jembi.bsis.model.packtype.PackType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PackTypeSerializer extends JsonSerializer<PackType> {

  @Override
  public void serialize(PackType packType, JsonGenerator jsonGenerator, SerializerProvider provider)
      throws IOException, JsonProcessingException {

    if (packType == null || packType.getId() == null) {
      jsonGenerator.writeNull();
      return;
    }

    jsonGenerator.writeString(packType.getId().toString());
  }
}
