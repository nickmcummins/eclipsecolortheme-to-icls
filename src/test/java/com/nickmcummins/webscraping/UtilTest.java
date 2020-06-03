package com.nickmcummins.webscraping;

import org.testng.annotations.Test;

import static com.nickmcummins.webscraping.org.eclipsecolorthemes.TestUtil.loadResourceAsString;
import static org.testng.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testReplaceValueOfXmlElement() throws Exception {
        String originalIcls = loadResourceAsString("github_new.icls");
        String expectedIcls = loadResourceAsString("github_new_modified_replaced.icls");

        String replacedIcls = Util.replaceValueOfXmlElement(originalIcls, "property", "name", "modified", "2020-05-13T14:06:05");

        assertEquals(replacedIcls, expectedIcls);
    }
}
