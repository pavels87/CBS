package com.cbs.controller.utils.instant;

import com.cbs.controller.utils.DateUtils;
import com.cbs.enums.ErrorMessageCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Instant;

import java.io.IOException;

/**
 * User: PSpiridonov
 * Date: 25.05.15
 * Time: 16:24
 */
public class InstantDeserializer extends JsonDeserializer<Instant> implements ContextualDeserializer {

    public static final String DEFAULT_PATTERN = DateUtils.Patterns.DEFAULT_JS_FORMAT;

    private final String pattern;
    private final String fieldName;

    public InstantDeserializer(String pattern, String fieldName) {
        this.pattern = pattern;
        this.fieldName = fieldName;
    }

    public InstantDeserializer() {
        pattern = DEFAULT_PATTERN;
        fieldName = null;
    }

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateStr = jsonParser.getText().trim();
        try {
            Instant instant = DateUtils.dateStringToInstant(dateStr, pattern);
            return instant;
        } catch (Throwable e) {
            throw new ParseJsonException(fieldName, ErrorMessageCode.INVALID_DATE_FORMAT.name(), pattern);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        String beanPropertyName = beanProperty.getName();
        DatePattern annotation = beanProperty.getAnnotation(DatePattern.class);
        String pattern = annotation == null ? null : annotation.value();
        pattern = StringUtils.isBlank(pattern) ? DEFAULT_PATTERN : pattern;
        return new InstantDeserializer(pattern, beanPropertyName);
    }
}
