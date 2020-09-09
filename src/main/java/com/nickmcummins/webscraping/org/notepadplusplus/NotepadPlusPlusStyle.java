package com.nickmcummins.webscraping.org.notepadplusplus;

import java.util.List;

public class NotepadPlusPlusStyle {


    class LexerType {
        private String name;
        private String ext;
        private List<WordsStyle> wordsStyles;
    }

    static class WordsStyle {
        enum Name {
            PREPROCESSOR,
            DEFAULT,
            INSTRUCTION_WORD,
            TYPE_WORD,
            NUMBER,
            STRING,
            CHARACTER,
            OPERATOR,
            VERBATIM,
            REGEX,
            COMMENT,
            COMMENT_LINE,
            COMMENT_DOC,
            COMMENT_LINE_DOC,
            COMMENT_DOC_KEYWORD,
            COMMENT_DOC_KEYWORD_ERROR;
        }

        public String name;
        public String styleID;
        public String fgColor;
        public String bhColor;
        public String fontName;
        public String fontStyle;
        public String fontSize;
    }
}