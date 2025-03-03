package org.imdb.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class JsonUtils {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules()
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    .registerModule(new JavaTimeModule())
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  private JsonUtils() {
    throw new UnsupportedOperationException("Cannot instantiate utility class.");
  }

  public static <T> List<T> readArrayFromJson(String path, TypeReference<List<T>> typeReference) {
    try {
      return OBJECT_MAPPER.readValue(new File(path), typeReference);
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      return Collections.emptyList();
    }
  }

  public static String toJson(Object object) {
    try {
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      return object.getClass().getSimpleName() + "@" + Integer.toHexString(object.hashCode());
    }
  }
}
