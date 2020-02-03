package net.drtz.marsattacks;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateNormalizerTest {

    @Test
    void testNormalizeDateString() throws DateParseException {
        DateNormalizer normalizer = new DateNormalizer(DateTimeFormatter.ISO_LOCAL_DATE);

        // core tests
        assertEquals("2017-02-27", normalizer.normalizeDateString("02/27/17"));
        assertEquals("2018-06-02", normalizer.normalizeDateString("June 2, 2018"));
        assertEquals("2016-07-13", normalizer.normalizeDateString("Jul-13-2016"));

        // additional tests
        assertEquals("2018-04-30", normalizer.normalizeDateString("April 30, 2018"));
        assertEquals("2018-04-30", normalizer.normalizeDateString("Apr-30-2018"));
        assertEquals("2020-02-29", normalizer.normalizeDateString("February 29, 2020"));

        // nonexistent date
        assertThrows(DateParseException.class, () -> { normalizer.normalizeDateString("April 31, 2018"); });
        assertThrows(DateParseException.class, () -> { normalizer.normalizeDateString("February 29, 2018"); });

        // invalid format
        assertThrows(DateParseException.class, () -> { normalizer.normalizeDateString("April 30 2018"); });
    }

}