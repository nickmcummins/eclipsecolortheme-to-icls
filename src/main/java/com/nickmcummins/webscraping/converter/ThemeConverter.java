package com.nickmcummins.webscraping.converter;

public interface ThemeConverter<T, F> {
    F convert(T inputTheme);
}
