package com.github.arturkovalchuk.intellijplatformpluginopenincursor.cursor

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.TimeUnit

/**
 * Hands `cursor://` URLs to the OS protocol handler via [BrowserUtil.browse].
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
        LOG.info("openFile requested: projectBasePath=$projectBasePath, file=$fileAbsolutePath, line=$lineNumber")
        val fileUrl = CursorUrlBuilder.forPath(fileAbsolutePath, lineNumber)

        if (projectBasePath.isNullOrBlank() || projectBasePath == fileAbsolutePath) {
            LOG.info("Direct open (no chaining): projectBasePath=${projectBasePath ?: "<null>"}, fileUrl=$fileUrl")
            launch(fileUrl)
            return
        }

        val projectUrl = CursorUrlBuilder.forPath(projectBasePath)
        LOG.info("Chained open: $projectUrl -> $fileUrl (delay ${PROJECT_FOCUS_DELAY_MS}ms)")
        launch(projectUrl)
        AppExecutorUtil.getAppScheduledExecutorService().schedule(
            {
                LOG.info("Chained open: firing delayed file URL $fileUrl")
                launch(fileUrl)
            },
            PROJECT_FOCUS_DELAY_MS,
            TimeUnit.MILLISECONDS,
        )
    }

    fun openPath(absolutePath: String) {
        LOG.info("openPath requested: path=$absolutePath")
        launch(CursorUrlBuilder.forPath(absolutePath))
    }

    private fun launch(url: String) {
        LOG.info("Launching $url via BrowserUtil.browse")
        BrowserUtil.browse(url)
    }
}
