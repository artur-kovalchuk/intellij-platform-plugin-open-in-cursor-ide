package com.github.arturkovalchuk.intellijplatformpluginopenincursor.cursor

import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.concurrency.AppExecutorUtil
import java.awt.Desktop
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * Hands `cursor://` URLs to the OS protocol handler.
 *
 * For per-file launches we fire the project folder URL first, then the file URL ~350 ms later.
 * Cursor inherits VS Code's URL service which has no `?newWindow=true` parameter and always
 * targets the most-recently-focused window — so without the chained call, opening a file from
 * an IDE project that isn't currently focused in Cursor would shove the file into the wrong
 * window. The folder URL forces Cursor to focus (or open) the right workspace first.
 */
object CursorLauncher {

    private val LOG = Logger.getInstance(CursorLauncher::class.java)
    private const val PROJECT_FOCUS_DELAY_MS = 350L

    fun openFile(projectBasePath: String?, fileAbsolutePath: String, lineNumber: Int?) {
        val fileUrl = CursorUrlBuilder.forPath(fileAbsolutePath, lineNumber)

        if (projectBasePath.isNullOrBlank() || projectBasePath == fileAbsolutePath) {
            launch(fileUrl)
            return
        }

        val projectUrl = CursorUrlBuilder.forPath(projectBasePath)
        LOG.info("Chained open: $projectUrl -> $fileUrl (delay ${PROJECT_FOCUS_DELAY_MS}ms)")
        launch(projectUrl)
        AppExecutorUtil.getAppScheduledExecutorService().schedule(
            { launch(fileUrl) },
            PROJECT_FOCUS_DELAY_MS,
            TimeUnit.MILLISECONDS,
        )
    }

    fun openPath(absolutePath: String) {
        launch(CursorUrlBuilder.forPath(absolutePath))
    }

    private fun launch(url: String) {
        LOG.info("Opening $url")
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
                return
            }
            execFallback(url)
        } catch (e: Exception) {
            LOG.warn("Desktop.browse failed for $url, trying OS fallback", e)
            try {
                execFallback(url)
            } catch (fallbackErr: Exception) {
                LOG.warn("OS fallback failed for $url", fallbackErr)
            }
        }
    }

    private fun execFallback(url: String) {
        val os = System.getProperty("os.name").lowercase()
        val command = when {
            os.contains("mac") -> arrayOf("open", url)
            os.contains("win") -> arrayOf("rundll32", "url.dll,FileProtocolHandler", url)
            else -> arrayOf("xdg-open", url)
        }
        Runtime.getRuntime().exec(command)
    }
}
