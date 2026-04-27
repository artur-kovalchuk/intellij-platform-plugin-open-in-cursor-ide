package com.github.arturkovalchuk.intellijplatformpluginopenincursor.actions

import com.github.arturkovalchuk.intellijplatformpluginopenincursor.OpenInCursorBundle
import com.github.arturkovalchuk.intellijplatformpluginopenincursor.cursor.CursorLauncher
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.vfs.VirtualFile

class OpenInCursorAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = e.project
        val target = resolveTarget(e)
        e.presentation.isEnabledAndVisible = project != null && target != null
        e.presentation.text = OpenInCursorBundle.message("action.openInCursor.text")
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val target = resolveTarget(e) ?: return

        if (target.isDirectory) {
            CursorLauncher.openPath(target.path)
            return
        }

        val lineNumber = resolveLineNumber(e, target)
        CursorLauncher.openFile(project.basePath, target.path, lineNumber)
    }

    private fun resolveTarget(e: AnActionEvent): VirtualFile? {
        e.getData(CommonDataKeys.VIRTUAL_FILE)?.let { return it }
        val multi = e.getData(PlatformCoreDataKeys.VIRTUAL_FILE_ARRAY)
        return multi?.singleOrNull()
    }

    private fun resolveLineNumber(e: AnActionEvent, target: VirtualFile): Int? {
        val editor: Editor = e.getData(CommonDataKeys.EDITOR) ?: return null
        val editorFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (editorFile != target) return null
        return editor.caretModel.logicalPosition.line + 1
    }
}
