package utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import model.packtype.PackType;

import java.io.IOException;

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
