package net.drtz.marsattacks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MarsAttacksControllerTests {

    @Test
    void testNormalizeDateString() {
        // core tests
        assertEquals("2017-02-27", MarsAttacksController.normalizeDateString("02/27/17"));
        assertEquals("2018-06-02", MarsAttacksController.normalizeDateString("June 2, 2018"));
        assertEquals("2016-07-13", MarsAttacksController.normalizeDateString("Jul-13-2016"));

        // additional tests
        assertEquals("2018-04-30", MarsAttacksController.normalizeDateString("April 30, 2018"));
        assertEquals("2018-04-30", MarsAttacksController.normalizeDateString("Apr-30-2018"));
        assertEquals("2020-02-29", MarsAttacksController.normalizeDateString("February 29, 2020"));

        // nonexistent date
        assertThrows(ResponseStatusException.class, () -> { MarsAttacksController.normalizeDateString("April 31, 2018"); });
        assertThrows(ResponseStatusException.class, () -> { MarsAttacksController.normalizeDateString("February 29, 2018"); });

        // invalid format
        assertThrows(ResponseStatusException.class, () -> { MarsAttacksController.normalizeDateString("April 30 2018"); });
    }

}
