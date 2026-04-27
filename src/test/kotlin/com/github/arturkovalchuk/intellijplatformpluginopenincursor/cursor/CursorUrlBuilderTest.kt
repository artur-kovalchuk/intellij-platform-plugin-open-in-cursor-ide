package com.github.arturkovalchuk.intellijplatformpluginopenincursor.cursor

import org.junit.Assert.assertEquals
import org.junit.Test

class CursorUrlBuilderTest {

    @Test
    fun forPath_absoluteFile_buildsCursorFileUrl() {
        val url = CursorUrlBuilder.forPath("/Users/me/proj/README.md")
        assertEquals("cursor://file/Users/me/proj/README.md", url)
    }

    @Test
    fun forPath_withLineNumber_appendsColonLine() {
        val url = CursorUrlBuilder.forPath("/Users/me/proj/README.md", lineNumber = 60)
        assertEquals("cursor://file/Users/me/proj/README.md:60", url)
    }

    @Test
    fun forPath_relativePath_isPromotedToAbsolute() {
        val url = CursorUrlBuilder.forPath("Users/me/proj")
        assertEquals("cursor://file/Users/me/proj", url)
    }

    @Test
    fun forPath_trailingSlash_isStripped() {
        val url = CursorUrlBuilder.forPath("/Users/me/proj/")
        assertEquals("cursor://file/Users/me/proj", url)
    }

    @Test
    fun forPath_zeroOrNegativeLine_isOmitted() {
        val zero = CursorUrlBuilder.forPath("/Users/me/proj/file.kt", lineNumber = 0)
        val negative = CursorUrlBuilder.forPath("/Users/me/proj/file.kt", lineNumber = -1)
        assertEquals("cursor://file/Users/me/proj/file.kt", zero)
        assertEquals("cursor://file/Users/me/proj/file.kt", negative)
    }

    @Test
    fun forPath_backslashes_areConvertedToForwardSlashes() {
        val url = CursorUrlBuilder.forPath("\\Users\\me\\proj\\file.kt")
        assertEquals("cursor://file/Users/me/proj/file.kt", url)
    }
}
