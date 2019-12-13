package io.kw.model.bar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

@AllArgsConstructor
@Getter
public enum Timeframe implements Serializable {
    M1(ChronoUnit.MINUTES, 1), M5(ChronoUnit.MINUTES, 5), M15(ChronoUnit.MINUTES, 15),
    M30(ChronoUnit.MINUTES, 30), H1(ChronoUnit.HOURS, 1), H2(ChronoUnit.HOURS, 2),
    H4(ChronoUnit.HOURS, 4), D(ChronoUnit.DAYS, 1), W(ChronoUnit.WEEKS, 1),
    M(ChronoUnit.MONTHS, 1);
    private ChronoUnit truncatedUnit;
    private int timePassed;

    public static boolean hasTimeChanged(Timeframe timeframe, Instant previous, Instant current) {
        Instant truncatedPrevious = previous.truncatedTo(timeframe.getTruncatedUnit());
        Instant truncatedCurrent = current.truncatedTo(timeframe.getTruncatedUnit());
        boolean timeHasChanged = false;
        int currentTime = getSpecifiedTimeUnit(current, timeframe.getTruncatedUnit());
        if (truncatedCurrent.compareTo(truncatedPrevious) != 0 &&
                currentTime % timeframe.getTimePassed() == 0) {
            timeHasChanged = true;
        }
        return timeHasChanged;
    }

    private static int getSpecifiedTimeUnit(Instant instant, ChronoUnit unit) {
        ZonedDateTime convertedTime = instant.atZone(ZoneOffset.UTC);
        Calendar calendar = GregorianCalendar.from(convertedTime);
        switch (unit) {
            case MINUTES: return calendar.get(Calendar.MINUTE);
            case HOURS: return calendar.get(Calendar.HOUR);
            case DAYS: return calendar.get(Calendar.DAY_OF_MONTH);
            case WEEKS: return calendar.get(Calendar.WEEK_OF_MONTH);
            case MONTHS: return calendar.get(Calendar.MONTH);
            default: return 0;
        }
    }
}
