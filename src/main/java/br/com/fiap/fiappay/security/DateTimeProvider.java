package br.com.fiap.fiappay.security;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public interface DateTimeProvider {

    LocalDateTime dateTime();

    LocalDate date();

    LocalTime time();

    Instant toDate(LocalDateTime localDateTime);

    LocalDateTime fromDate(Date date);

}
