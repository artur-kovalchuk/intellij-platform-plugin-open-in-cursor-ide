<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Open in Cursor Changelog

## [Unreleased]
### Added
- `Cursor` action added under IDEA's standard **Open In** submenu (`RevealGroup`) — appears in the editor popup, editor tab popup, project view popup, and navigation-bar popup alongside "Reveal in Finder".
- Chained `cursor://` launch: project folder URL is fired first, then the file URL ~350 ms later, so the file lands in the correct Cursor window.
- Folder targets (project root, sub-directories) open via a single project URL with no line number.
