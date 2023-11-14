package io.hoogland.weer2track.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Util class used for common operations to do with date or time.
 *
 * @author dan
 */
public class DateUtils {

    /**
     * Checks whether or not a given {@link DayOfWeek} is part of the weekend or not.
     *
     * @param day {@link DayOfWeek} to be checked
     * @return If the input is part of the weekend
     */
    public static boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
