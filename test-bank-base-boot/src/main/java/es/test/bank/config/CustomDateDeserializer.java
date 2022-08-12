package es.test.bank.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonComponent
public class CustomDateDeserializer extends StdDeserializer<Date> {
    
    private static final long serialVersionUID = 1L;
    private final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ"))
            .toFormatter();

    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        if (NumberUtils.isParsable(date)) {
            try {
                return new Date(Long.parseLong(date));
            } catch (Exception e) {
                LOGGER.trace("Long Unparseable date: \"" + date + "\"");
                throw new RuntimeException("LongUnparseable date: \"" + date + "\"");
            }
        }
        try {
        LocalDateTime dateToConvert = LocalDateTime.parse(jsonparser.getText(), dateFormatter);
        return java.sql.Timestamp.valueOf(dateToConvert);
        } catch (Exception e) {
            LOGGER.error("Error parsing date: \"" + date + "\" : ", e);
            throw new RuntimeException("Unparseable date: \"" + date + "\"");
        }
    }
    

}