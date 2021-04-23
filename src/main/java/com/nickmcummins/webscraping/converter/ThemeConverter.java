package com.nickmcummins.webscraping.converter;

import com.nickmcummins.webscraping.ColorTheme;
import com.nickmcummins.webscraping.org.eclipsecolorthemes.EclipseColorTheme;

public interface ThemeConverter<T>{
    ColorTheme convert(EclipseColorTheme theme);
}
