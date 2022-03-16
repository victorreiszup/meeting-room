package br.com.api.meetingroom.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public final class DateUltils {

    public static final ZoneOffset DEFAULT_OFFSET = ZoneOffset.of("-03:00");

    private DateUltils() {
    }

    public static OffsetDateTime newOffsetDateTimeNow() {
        return OffsetDateTime.now(DEFAULT_OFFSET).truncatedTo(ChronoUnit.MILLIS);
    }

    public static boolean isOverlapping(OffsetDateTime startAt1, OffsetDateTime endAt1,
                                        OffsetDateTime startAt2, OffsetDateTime endAt2) {

        return startAt1.compareTo(endAt2) < 0 && endAt1.compareTo(startAt2) > 0;

    }

}
