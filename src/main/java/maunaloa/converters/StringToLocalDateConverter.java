package maunaloa.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by rcs on 22.12.15.
 *
 */

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, dtf);
    }
}
