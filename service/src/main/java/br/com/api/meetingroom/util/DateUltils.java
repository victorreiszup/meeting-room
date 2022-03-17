package br.com.api.meetingroom.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public final class DateUltils {

    public static final ZoneOffset DEFAULT_OFFSET = ZoneOffset.of("-03:00");

    private DateUltils() {
    }

    public static LocalDateTime newLocalDateTimeNow() {
        return LocalDateTime.now(DEFAULT_OFFSET).truncatedTo(ChronoUnit.MINUTES);
    }

    public static boolean isOverlapping(LocalDateTime startAt1, LocalDateTime endAt1,
                                        LocalDateTime startAt2, LocalDateTime endAt2) {

        return startAt1.compareTo(endAt2) < 0 && endAt1.compareTo(startAt2) > 0;

    }

}
