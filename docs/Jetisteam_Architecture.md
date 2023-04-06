### Jetisteam Architecture

- **app** the main application launcher
- **data** kSteam and common utilities
- **uikit** common Compose utilities
- **microapp** features

### Microapp Structure

- **library** can be consisting of modules :microapp:library:home (user-facing library), :microapp:library:remote (for managing remote Steam clients) and :microapp:library:profile (for non-user libraries)
- **store** can be consisting of modules :microapp:store:storefront (store), :microapp:store:page-game (a specific game page) and :microapp:store:reviews (a game reviews)
- **community** can be consisting of modules :microapp:community:hub (game hubs) and :microapp:community:news (game news)

The idea is to decouple large screen groups into a lot of smaller modules to:
- allow opening of each other (still issues but will be much easier) - impossible to do NIA way beacuse of DI
- dynamic features (I don't know of any size benefit, but okay)