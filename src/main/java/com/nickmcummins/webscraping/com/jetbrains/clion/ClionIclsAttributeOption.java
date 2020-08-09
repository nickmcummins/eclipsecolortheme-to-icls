package com.nickmcummins.webscraping.com.jetbrains.clion;

import com.nickmcummins.webscraping.com.jetbrains.IclsAttributeOption;

import java.util.Map;

public class ClionIclsAttributeOption {

    public enum Name implements IclsAttributeOption.Name {
        ENUM_CONSTANT,
        KEYWORD,
        STRUCT_FIELD,
        STRUCT_LIKE,
        TEMPLATE_TYPE;

        private final Map<String, String> colorMapper;

        Name(Map<String, String> colorMapper) {
            this.colorMapper = colorMapper;
        }

        Name() {
            this(null);
        }

        @Override
        public Map<String, String> getColorMapper() {
            return null;
        }
    }
}
