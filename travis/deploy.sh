#!/usr/bin/env bash

set -e

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ] && [ ! -z "$TRAVIS_TAG" ]; then
	echo "deploying tag: $TRAVIS_TAG"
    mvn versions:set -DnewVersion=${TRAVIS_TAG}
    mvn --settings travis/settings.xml clean deploy -B -U -P release
else
	echo "no tag so no deploy"
fi