package io.hoogland.weer2track.model.converter;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import io.hoogland.weer2track.util.DateUtils;

/**
 * Converter to allow {@link LocalDate} and {@link LocalDateTime} to be used in models
 * and saved as {@link Long} in the database.
 *
 * @author dan
 */
public class DateConverter {

    /**
     * Convert epoch ({@link Long}) to {@link LocalDateTime}.
     *
     * @param epoch Time since epoch
     * @return {@link LocalDateTime} based on the input value since epoch
     */
    @TypeConverter
    public static LocalDateTime epochToLocalDateTime(Long epoch) {
        return epoch == null ? null : LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }

    /**
     * Convert {@link LocalDateTime} to epoch ({@link Long}).
     *
     * @param dateTime {@link LocalDateTime} to be converted
     * @return Seconds since epoch as {@link Long}
     */
    @TypeConverter
    public static Long localDateTimeToEpoch(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * Convert days since epoch as {@link Long} to {@link LocalDate}.
     *
     * @param daysSinceEpoch days since epoch to be converted
     * @return {@link LocalDate} with the value of the input days since epoch
     */
    @TypeConverter
    public static LocalDate epochToLocalDate(Long daysSinceEpoch) {
        return daysSinceEpoch == null ? null : LocalDate.ofEpochDay(daysSinceEpoch);
    }

    /**
     * Convert {@link LocalDate} to days since epoch as {@link Long}.
     *
     * @param localDate {@link LocalDate} to be converted
     * @return Days since epoch as {@link Long}
     */
    @TypeConverter
    public static Long localDateToEpochDay(LocalDate localDate) {
        return localDate == null ? null : localDate.toEpochDay();
    }


}
