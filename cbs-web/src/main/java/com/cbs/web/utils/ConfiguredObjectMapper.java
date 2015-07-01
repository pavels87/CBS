package com.cbs.web.utils;

import com.cbs.web.utils.instant.InstantDeserializer;
import com.cbs.web.utils.instant.InstantSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.Instant;


public class ConfiguredObjectMapper extends ObjectMapper {

    public ConfiguredObjectMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final SimpleModule module = new SimpleModule("", Version.unknownVersion());
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addDeserializer(Instant.class, new InstantDeserializer());
        registerModule(module);
    }
}
