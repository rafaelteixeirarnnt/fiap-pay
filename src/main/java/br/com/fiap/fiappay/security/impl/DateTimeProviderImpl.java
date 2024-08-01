package br.com.fiap.fiappay.security.impl;

import br.com.fiap.fiappay.security.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime dateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate date() {
        return LocalDate.now();
    }

    @Override
    public LocalTime time() {
        return LocalTime.now();
    }

    @Override
    public Instant toDate(LocalDateTime localDateTime) {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    @Override
    public LocalDateTime fromDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
