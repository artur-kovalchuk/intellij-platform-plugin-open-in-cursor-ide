<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# intellij-platform-plugin-open-in-cursor Changelog

## [Unreleased]

Replace manual os.name detection + Runtime.exec("open"/"rundll32"/
"xdg-open") with com.intellij.ide.BrowserUtil.browse(), the platform
API for protocol-handler dispatch.

Fixes silent failures on environments where the hardcoded fallback
binary is missing or restricted (Linux distros without xdg-open,
sandboxed/locked-down Windows), and surfaces failures via IntelliJ's
standard notification instead of just a log line.

## [1.0.2] - 2026-04-28

### Changed

- Update plugin icons with new SVG designs for light and dark themes

## [1.0.1] - 2026-04-28

### Changed

- Update IntelliJ platform version constraints in build.gradle.kts to support since build 243
- Update plugin.xml to set require-restart attribute to false

## [0.6.0] - 2026-04-27

### Added

- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- `Cursor` action added under IDEA's standard **Open In** submenu (`RevealGroup`) — appears in the editor popup, editor tab popup, project view popup, and navigation-bar popup alongside "Reveal in Finder".
- Chained `cursor://` launch: project folder URL is fired first, then the file URL ~350 ms later, so the file lands in the correct Cursor window.
- Folder targets (project root, sub-directories) open via a single project URL with no line number.
- Cursor logo icon (16×16 with transparent background, plus `@2x` HiDPI variant) shown next to the `Cursor` action item.

[Unreleased]: https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/compare/1.0.2...HEAD
[1.0.2]: https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/compare/0.6.0...1.0.1
[0.6.0]: https://github.com/artur-kovalchuk/intellij-platform-plugin-open-in-cursor/commits/0.6.0
