package com.nickmcummins.webscraping.org.eclipsecolorthemes;


import java.util.*;
import java.util.stream.Collectors;
import java.awt.Color;
import java.util.stream.Stream;

import static java.util.Arrays.asList;


public class EclipseColorThemeColorsComparer {
    private final Set<List<Integer>> colorRgbs;

    public EclipseColorThemeColorsComparer(EclipseColorTheme eclipseColorTheme) {
        this.colorRgbs = eclipseColorTheme.getSettingsByName().values().stream()
                .map(setting -> Color.decode(setting.getColorValue()))
                .map(color -> asList(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toSet());
    }

    private static List<Integer> colorSum(Integer[] rgbA, Integer[] rgbB) {
        return asList(rgbA[0] + rgbB[0], rgbA[1] + rgbB[1], rgbA[2] + rgbB[2]);
    }

    private static List<Integer> colorDiff(Integer[] rgbA, Integer[] rgbB) {
        return Stream.of(rgbA[0] -  rgbB[0], rgbA[1] - rgbB[1], rgbA[2] - rgbB[2])
                .map(Math::abs)
                .collect(Collectors.toList());

    }

    public List<List<Integer>> computeColorSumsAndDiffs() {
        List<List<Integer>> colorRgbsList = new ArrayList<>(colorRgbs);

        Set<List<Integer>> colorSumsAndDiffs = new HashSet<>();
        for (int i = 0; i < colorRgbsList.size(); i++) {
            for (int j = 0; j < colorRgbsList.size(); j++) {
                if (i != j) {
                    Integer[] first = colorRgbsList.get(i).toArray(new Integer[0]);
                    Integer[] second = colorRgbsList.get(j).toArray(new Integer[0]);
                    colorSumsAndDiffs.add(colorSum(first, second));
                    colorSumsAndDiffs.add(colorDiff(first, second));
                }
            }
        }

        return new ArrayList<>(colorSumsAndDiffs).stream().sorted(Comparator.comparingInt(list -> list.get(0))).collect(Collectors.toList());
    }

}
