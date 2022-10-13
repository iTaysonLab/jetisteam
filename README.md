# Jetisteam
_an unofficial and experimental alternative Steam frontend for Android_

## Disclaimer

This app is using WebUI RPC-like API - but Steam's API is not that complete at all: some features will be completely missing or be incomplete. Such as:
- reviews (heavy relies on HTML parsing because only AJAX API has access to reviews, we can only show user's own reviews)
- curators and publishers (no API at all)
- UGC content (can't get user's screenshots/guides without HTML parsing, because native API is locked even if official application OAuth token is used)
- home screen (no way to get it in one piece, maybe some kind of Steam Deck-styled store is possible with the current API)

And finally - **this app is not affiated, related, made or allowed by Valve Corporation. Please do not write to them about bugs or incorrect behavior of this application**.

## Features

- Steam Guard code generator/remote auth confirmator (incomplete, see #4 for the mega-issue)
- Profile screen with dynamic adaptation to your profile theme
- Library (incomplete)
- Game screen
- Notifications (barebones)
- Made with Jetpack Compose, Hilt, AndroidX and other fancy/modern stuff!

## Credits
- [SteamDB](https://github.com/SteamDatabase/Protobufs) for protobufs (they are slightly modified to fix Java generating - recovering enums/setting package names/enabling generic services)
- [MMKV](https://github.com/Tencent/MMKV) for saving account data
