# Jetisteam
_an unofficial and experimental alternative Steam frontend for Android_

## Construction Notice

This branch (ksteam) is the result of Jetisteam being rebuilt in [kSteam](https://github.com/iTaysonLab/kSteam) - a custom Steam Network wrapper built specially for this app.
If you want a more complete experience for now, please refer to the "main" branch (however, it won't be updated).

## Disclaimer

This app is using WebUI RPC-like API - but Steam's API is not that complete at all: some features will be completely missing or be incomplete. Such as:
- curators and publishers (no API at all)
- UGC content (can't get user's screenshots/guides without HTML parsing, because native API is locked even if official application OAuth token is used)
- home screen (no way to get it in one piece, maybe some kind of Steam Deck-styled store is possible with the current API)

However, some of this stuff could be achieved by using other sources of info - such as internal Web API or Steam3 WS API (messages are totally dependant on this).

Finally - **this app is not affiliated, related, made or allowed by Valve Corporation. Please do not write to them about bugs, incorrect behavior of this application or bans (that should not happen of course).**.

## Features

- Steam Guard code generator/remote auth confirmator (incomplete, see #4 for the mega-issue)
- Profile screen with dynamic adaptation to your profile theme
- Library
- Store page (in development)
- Notifications
- Made with Jetpack Compose, Hilt, AndroidX and other fancy/modern stuff!

## Credits
- [SteamDB](https://github.com/SteamDatabase/Protobufs) for protobufs (they are slightly modified to fix Java generating - recovering enums/setting package names/enabling generic services)
- [Revadike](https://github.com/Revadike/InternalSteamWebAPI/) for the excellent Internal Steam WebAPI documentation
- [MMKV](https://github.com/Tencent/MMKV) for saving account data
