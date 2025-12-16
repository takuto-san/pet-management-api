package org.springframework.petmanagement.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class DateTimeMapper {

    public OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(java.time.ZoneOffset.UTC);
    }

    public LocalDateTime map(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }
}
