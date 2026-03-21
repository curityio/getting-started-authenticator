#!/bin/bash

#
# Creates a new authenticator plugin from the root example template.
# Pulls the latest upstream changes before copying.
#
# Usage: ./new-authenticator.sh <authenticator-name>
# Example: ./new-authenticator.sh my-saml-authenticator
#

set -e

NAME="${1:?Usage: $0 <authenticator-name>}"
DEST="authenticators/$NAME"

if [ -d "$DEST" ]; then
  echo "Error: $DEST already exists"
  exit 1
fi

#
# Sync latest changes from the upstream template
#
echo "Fetching latest changes from upstream (curityio/getting-started-authenticator)..."
git fetch upstream

MERGE_BASE=$(git merge-base HEAD upstream/main)
UPSTREAM_HEAD=$(git rev-parse upstream/main)

if [ "$MERGE_BASE" = "$UPSTREAM_HEAD" ]; then
  echo "Already up to date with upstream."
else
  echo "Merging upstream changes..."
  if ! git merge upstream/main --no-edit; then
    echo ""
    echo "Merge conflict with upstream. Resolve conflicts with 'git mergetool',"
    echo "then run this script again."
    exit 1
  fi
fi

#
# Copy template into new plugin directory (excluding local-only files)
#
mkdir -p "$DEST"
cp -r src "$DEST/"
cp pom.xml "$DEST/"
rsync -a --exclude='license.json' --exclude='data/' --exclude='build/' deployment/ "$DEST/deployment/"

#
# Fix the copied deploy.sh:
#   - resolve git root properly (not relative ../.git which breaks from a subdirectory)
#   - find the built jar dynamically instead of hardcoding the artifact name
#
python3 - "$DEST/deployment/deploy.sh" <<'PYEOF'
import sys

path = sys.argv[1]
content = open(path).read()
original = content

PATCHES = [
    (
        'cp ./hooks/pre-commit ../.git/hooks',
        'GIT_ROOT=$(git -C . rev-parse --show-toplevel)\ncp ./hooks/pre-commit "${GIT_ROOT}/.git/hooks/pre-commit"'
    ),
    (
        'cp ./target/example-authenticator-1.0.0-SNAPSHOT.jar ./deployment/build/example-authenticator-1.0.0-SNAPSHOT.jar',
        'mkdir -p ./deployment/build\nJAR=$(find ./target -maxdepth 1 -name "*.jar" | head -1)\nif [ -z "$JAR" ]; then\n  echo \'Could not find plugin jar in target directory\'\n  exit 1\nfi\ncp "$JAR" ./deployment/build/plugin.jar'
    ),
]

warnings = []
for old, new in PATCHES:
    if old in content:
        content = content.replace(old, new)
    else:
        warnings.append(f"WARNING: Could not patch deploy.sh — expected string not found:\n  {old!r}\n  The upstream template may have changed. Review {path} manually.")

open(path, 'w').write(content)

for w in warnings:
    print(w, file=sys.stderr)

sys.exit(1 if warnings else 0)
PYEOF

#
# Fix the copied docker-compose.yml to use a generic jar name
#
python3 - "$DEST/deployment/docker-compose.yml" <<'PYEOF'
import sys, re

path = sys.argv[1]
content = open(path).read()

patched, count = re.subn(
    r'\./build/[^:]+\.jar:/opt/idsvr/usr/share/plugins/[^/\n]+/[^\n]+',
    './build/plugin.jar:/opt/idsvr/usr/share/plugins/authenticator/plugin.jar',
    content
)

if count == 0:
    print(f"WARNING: Could not patch docker-compose.yml — jar volume mount not found. Review {path} manually.", file=sys.stderr)
    sys.exit(1)

open(path, 'w').write(patched)
PYEOF

#
# Update artifactId and name in the copied pom.xml
#
python3 - "$DEST/pom.xml" "$NAME" <<'PYEOF'
import sys

path, name = sys.argv[1], sys.argv[2]
content = open(path).read()
content = content.replace('<artifactId>example-authenticator</artifactId>', f'<artifactId>{name}</artifactId>')
content = content.replace('<name>Curity Example Authenticator</name>', f'<name>{name}</name>')
open(path, 'w').write(content)
PYEOF

echo ""
echo "Created $DEST from the latest upstream template."
echo ""
echo "Next steps:"
echo "  1. Update groupId in $DEST/pom.xml"
echo "  2. Rename the Kotlin package from com.example.plugins.exampleauthenticator"
echo "  3. Add your Curity config to $DEST/deployment/resources/curity-config.xml"
echo "  4. Copy your license.json into $DEST/deployment/"
echo ""
echo "Build:  cd $DEST && mvn package"
echo "Deploy: $DEST/deployment/deploy.sh"
