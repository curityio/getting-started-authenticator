# Authenticator Plugins

This repo is a fork of [curityio/getting-started-authenticator](https://github.com/curityio/getting-started-authenticator).
The root contains the upstream example template. Each plugin lives under `authenticators/`.

## Create a new plugin

```bash
./new-authenticator.sh <name>
```

This will:
1. Pull the latest changes from upstream into your current branch
2. Copy the example template into `authenticators/<name>/`
3. Patch the deployment scripts to work correctly from a subdirectory

Then complete the setup:

1. Set `groupId` in `authenticators/<name>/pom.xml`
2. Rename the Kotlin package from `com.example.plugins.exampleauthenticator`
3. Add your Curity config to `authenticators/<name>/deployment/resources/curity-config.xml`
4. Copy your `license.json` into `authenticators/<name>/deployment/`

## Build a plugin

```bash
cd authenticators/<name>
mvn package
```

## Deploy a plugin

```bash
authenticators/<name>/deployment/deploy.sh
```

Requires: Java 21, Maven, Docker, ngrok, jq.

## Sync upstream template changes

Upstream changes are pulled automatically when you run `new-authenticator.sh`.
To sync manually at any time:

```bash
git fetch upstream
git merge upstream/main
```

The root `src/`, `pom.xml`, `deployment/`, and `README.md` are owned by upstream — don't modify them directly.
