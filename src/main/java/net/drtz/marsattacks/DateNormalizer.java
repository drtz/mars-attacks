package net.drtz.marsattacks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class DateNormalizer {

    DateTimeFormatter formatter;

    public DateNormalizer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    // Define a set of parsers for accepted date formats
    private static List<DateTimeFormatter> dateFormatters = List.of(
            DateTimeFormatter.ofPattern("MM/dd/uu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMMM d, u").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMM-d-uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("u-M-d").withResolverStyle(ResolverStyle.STRICT)
    );

    public String normalizeDateString(String dateString) throws DateParseException {
        for (DateTimeFormatter dateFormatter : dateFormatters) {
            try {
                LocalDate date = LocalDate.parse(dateString, dateFormatter);
                return this.formatter.format(date);
            } catch (DateTimeParseException e) {
                // skip over this parser and try the next one
            }
        }
        throw new DateParseException("invalid date: " + dateString);
    }

}
