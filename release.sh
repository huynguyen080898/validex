#!/bin/bash
set -e

if [ -z "$1" ]; then
  echo "Usage: ./release.sh <release-version>"
  echo "Example: ./release.sh 1.0.0"
  exit 1
fi

RELEASE_VERSION=$1

# next dev version
IFS='.' read -r MAJOR MINOR PATCH <<< "$RELEASE_VERSION"
NEXT_PATCH=$((PATCH + 1))
NEXT_DEV_VERSION="$MAJOR.$MINOR.$NEXT_PATCH-SNAPSHOT"

# Create tag
TAG_NAME="v$RELEASE_VERSION"

echo "Release version: $RELEASE_VERSION"
echo "Next development version: $NEXT_DEV_VERSION"
echo "Git tag: $TAG_NAME"

# Checkout main và pull mới nhất
git checkout main
git pull origin main

# Prepare release (commit version + tag + bump next SNAPSHOT)
mvn release:prepare \
    -DreleaseVersion=$RELEASE_VERSION \
    -DdevelopmentVersion=$NEXT_DEV_VERSION \
    -Dtag=$TAG_NAME \
    -B

# Perform release (deploy)
mvn release:perform -B

echo "Release $RELEASE_VERSION completed successfully with tag $TAG_NAME!"
