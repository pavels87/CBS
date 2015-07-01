package com.cbs.controller.utils.instant;

import com.contmatic.g5.phoenix.utils.date.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Instant;

import java.io.IOException;

/**
 * User: PSpiridonov
 * Date: 25.05.15
 * Time: 16:01
 */
public class InstantSerializer extends JsonSerializer<Instant> implements ContextualSerializer {

    public static final String DEFAULT_PATTERN = DateUtils.Patterns.DEFAULT_JS_FORMAT;

    private final String pattern;

    public InstantSerializer(String pattern) {
        this.pattern = pattern;
    }

    public InstantSerializer() {
        pattern = DEFAULT_PATTERN;
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (instant != null) {
            jsonGenerator.writeString(DateUtils.instantToDateString(instant, pattern, null));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        DatePattern annotation = beanProperty.getAnnotation(DatePattern.class);
        String pattern = annotation == null ? null : annotation.value();
        pattern = StringUtils.isBlank(pattern) ? DEFAULT_PATTERN : pattern;
        return new InstantSerializer(pattern);
    }
}
