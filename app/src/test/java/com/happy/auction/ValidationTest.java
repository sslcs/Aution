package com.happy.auction;

import com.happy.auction.utils.Validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidationTest {
    @Test
    public void validPassword() throws Exception {
        assertFalse(Validation.password("12345"));
        assertFalse(Validation.password("1234567890123456789"));
        assertFalse(Validation.password("123456~"));
        assertFalse(Validation.password("123456("));
        assertFalse(Validation.password("123456)"));
        assertFalse(Validation.password("123456-"));
        assertFalse(Validation.password("123456="));
        assertFalse(Validation.password("123456+"));
        assertFalse(Validation.password("123456,"));
        assertFalse(Validation.password("123456."));

        assertTrue(Validation.password("123456"));
        assertTrue(Validation.password("123456789012345678"));
        assertTrue(Validation.password("123456a"));
        assertTrue(Validation.password("123456A"));
        assertTrue(Validation.password("123456_!@#$%^&*"));
    }

    @Test
    public void validPhone() throws Exception {
        assertFalse(Validation.phone("1234567890"));
        assertFalse(Validation.phone("123456789012"));
        assertFalse(Validation.phone("20123456789"));

        assertTrue(Validation.phone("13212345678"));
        assertTrue(Validation.phone("15512345678"));
        assertTrue(Validation.phone("18812345678"));
        assertTrue(Validation.phone("14712345678"));
        assertTrue(Validation.phone("17612345678"));
        assertTrue(Validation.phone("19912345678"));
    }
}