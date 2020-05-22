package com.nickmcummins.webscraping;

import org.testng.annotations.Test;

import java.awt.*;

import static com.nickmcummins.webscraping.ColorUtil.*;
import static org.testng.Assert.*;

public class ColorUtilTest {
    @Test(enabled = false)
    public static void testColorDistance() {
        double colorDistanceA = colorDistance("430400", "a18280");
        double colorDistanceB = colorDistance("000000", "808080");
        assertEquals(colorDistanceA, colorDistanceB);
    }

    @Test
    public static void testIsDark() {
        assertTrue(isDark(Color.decode("#000000")));
    }

    @Test
    public static void testIsLight() {
        assertFalse(isLight(Color.decode("#000000")));

    }

    @Test
    public static void testSameColor() {
        assertTrue(sameColor("#ffffff", "FFFFFF"));
    }
}