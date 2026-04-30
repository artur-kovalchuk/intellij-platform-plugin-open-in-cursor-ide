# Open in Cursor IDE

![Build](https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/31482-open-in-cursor-ide.svg)](https://plugins.jetbrains.com/plugin/31482-open-in-cursor-ide)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/31482-open-in-cursor-ide.svg)](https://plugins.jetbrains.com/plugin/31482-open-in-cursor-ide)

<!-- Plugin description -->
Open the current file (or any file/folder from the Project view) directly in [Cursor IDE](https://cursor.com).

The action is added to IDEA's standard **Open In** submenu (`RevealGroup`), so it appears wherever IDEA already shows "Reveal in Finder":
- editor right-click → **Open In → Cursor** (uses the caret line),
- editor tab right-click → **Open In → Cursor**,
- Project view right-click → **Open In → Cursor** (also works on folders),
- navigation bar popup → **Open In → Cursor**.

It builds a `cursor://file<absolute-path>[:line]` URL — the same scheme Cursor inherits from VS Code — and hands it to the OS protocol handler. No configuration required: the IDE already knows the project root, file path, and caret line.

To make sure the file lands in the right Cursor window (and not whichever window Cursor focused last), the action fires the project folder URL first and then the file URL ~350 ms later. Combined with Cursor's `"window.openFoldersInNewWindow": "on"` setting, this opens a new Cursor window per project without disturbing the previously focused one.
<!-- Plugin description end -->

## Examples

| Where you click          | URL fired                                                                |
|--------------------------|--------------------------------------------------------------------------|
| Editor at line 60        | `cursor://file/Users/me/proj/README.md:60` (after `cursor://file/Users/me/proj`) |
| Editor tab               | `cursor://file/Users/me/proj/README.md` (after the project URL)          |
| Project view → file      | `cursor://file/Users/me/proj/src/Main.kt` (after the project URL)        |
| Project view → folder    | `cursor://file/Users/me/proj/src` (single fire, no chaining)             |

## Tips

- For each click to land in a fresh Cursor window without touching the previous one, set in Cursor: `Cmd+,` → search `openFoldersInNewWindow` → set **Window: Open Folders In New Window** to `on` (or `"window.openFoldersInNewWindow": "on"` in `settings.json`). Keep `window.openFilesInNewWindow` at `default` so the second leg of the chain reuses the freshly-opened window instead of spawning another empty one.
- Cursor must be installed and registered as the `cursor://` protocol handler. On macOS this happens automatically the first time Cursor runs.

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Open in Cursor IDE"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
