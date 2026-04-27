package com.github.arturkovalchuk.intellijplatformpluginopenincursor.cursor

/**
 * Builds `cursor://` URLs that the Cursor IDE registers as an OS-level URL handler.
 *
 * Format: `cursor://file<absolutePath>[:<line>]` — same scheme VS Code inherits, with a fixed
 * `file` host. Path is always absolute and forward-slashed.
 */
object CursorUrlBuilder {

    private const val FILE_HOST = "file"

    fun forPath(absolutePath: String, lineNumber: Int? = null): String {
        val normalized = normalize(absolutePath)
        val base = "cursor://$FILE_HOST$normalized"
        return if (lineNumber != null && lineNumber > 0) "$base:$lineNumber" else base
    }

    private fun normalize(rawPath: String): String {
        val trimmed = rawPath.trim().replace('\\', '/').trimEnd('/')
        if (trimmed.isEmpty()) return ""
        return if (trimmed.startsWith("/")) trimmed else "/$trimmed"
    }
}
