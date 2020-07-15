package com.nickmcummins.webscraping;


/*
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA  02111-1307, USA.
 */

import java.awt.Color;
import java.util.List;


/**
 * Common color utilities.
 *
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class ColorUtil {

    /**
     * Make a color darker.
     *
     * @param color    Color to make darker.
     * @param fraction Darkness fraction.
     * @return Darker color.
     */
    public static Color darker(Color color, double fraction) {
        int red = (int) Math.round(color.getRed() * (1.0 - fraction));
        int green = (int) Math.round(color.getGreen() * (1.0 - fraction));
        int blue = (int) Math.round(color.getBlue() * (1.0 - fraction));

        return getColor(color, red, green, blue);
    }


    /**
     * Make a color lighter.
     *
     * @param color    Color to make lighter.
     * @param fraction Darkness fraction.
     * @return Lighter color.
     */
    public static Color lighter(Color color, double fraction) {
        int red = (int) Math.round(color.getRed() * (1.0 + fraction));
        int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
        int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));

        return getColor(color, red, green, blue);
    }

    private static Color getColor(Color color, int red, int green, int blue) {
        if (red < 0) red = 0;
        else if (red > 255) red = 255;
        if (green < 0) green = 0;
        else if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        else if (blue > 255) blue = 255;

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);
    }


    /**
     * Return the hex name of a specified color.
     *
     * @param color Color to get hex name of.
     * @return Hex name of color: "rrggbb".
     */
    public static String getHexName(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        String rHex = Integer.toString(r, 16);
        String gHex = Integer.toString(g, 16);
        String bHex = Integer.toString(b, 16);

        return (rHex.length() == 2 ? "" + rHex : "0" + rHex) +
                (gHex.length() == 2 ? "" + gHex : "0" + gHex) +
                (bHex.length() == 2 ? "" + bHex : "0" + bHex);
    }

    /**
     * Return the "distance" between two colors. The rgb entries are taken
     * to be coordinates in a 3D space [0.0-1.0], and this method returnes
     * the distance between the coordinates for the first and second color.
     *
     * @param r1, g1, b1  First color.
     * @param r2, g2, b2  Second color.
     * @return Distance bwetween colors.
     */
    public static double colorDistance(double r1, double g1, double b1,
                                       double r2, double g2, double b2) {
        double a = r2 - r1;
        double b = g2 - g1;
        double c = b2 - b1;

        return Math.sqrt(a * a + b * b + c * c);
    }

    public static double colorDistance(Color colorA, Color colorB) {
        return colorDistance(colorA.getRed(), colorA.getGreen(), colorA.getBlue(),
                colorB.getRed(), colorB.getGreen(), colorB.getBlue());
    }

    public static double colorDistance(String hexA, String hexB) {
        if (!hexA.startsWith("#")) hexA = String.format("#%s", hexA);
        if (!hexB.startsWith("#")) hexB = String.format("#%s", hexB);
        return colorDistance(Color.decode(hexA), Color.decode(hexB));
    }

    /**
     * Check if a color is more dark than light. Useful if an entity of
     * this color is to be labeled: Use white label on a "dark" color and
     * black label on a "light" color.
     *
     * @param r,g,b Color to check.
     * @return True if this is a "dark" color, false otherwise.
     */
    public static boolean isDark(double r, double g, double b) {
        // Measure distance to white and black respectively
        double dWhite = ColorUtil.colorDistance(r, g, b, 1.0, 1.0, 1.0);
        double dBlack = ColorUtil.colorDistance(r, g, b, 0.0, 0.0, 0.0);

        return dBlack < dWhite;
    }


    /**
     * Check if a color is more dark than light. Useful if an entity of
     * this color is to be labeled: Use white label on a "dark" color and
     * black label on a "light" color.
     *
     * @param color Color to check.
     * @return True if this is a "dark" color, false otherwise.
     */
    public static boolean isDark(Color color) {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;

        return isDark(r, g, b);
    }

    public static boolean isLight(Color color) {
        return !isDark(color);
    }

    public static String formatHexValue(String hexColor) {
        String formattedHex = hexColor.replaceFirst("#", "").toLowerCase();
        int numZeroDigits = 0;
        int i = 0;
        while (numZeroDigits == i && i < formattedHex.length()) {
            if (formattedHex.charAt(i) == '0')
                numZeroDigits += 1;
            i += 1;
        }

        if (numZeroDigits == i)
            return String.valueOf(formattedHex.charAt(formattedHex.length() - 1));
        else
            return formattedHex.substring(numZeroDigits);
    }

    public static String rgbString(Color color) {
        return String.format("rgb(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static boolean sameColor(String hexColorA, String hexColorB) {
        return hexColorA.replace("#", "").toLowerCase().equals(hexColorB.replace("#", "").toLowerCase());
    }

    public static boolean listContainsColor(List<String> hexColors, String hexColor) {
        return hexColors.stream().anyMatch(color -> sameColor(color, hexColor));
    }
}

