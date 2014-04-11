#!/bin/bash

HOME_DIR=$PWD
DIR=$(basename $PWD)-pages
URL=$(git config --get remote.origin.url)

# Delete local branch pages's directory
cd ..
rm -rf $DIR

# Check out pages branch
git clone $URL $DIR

# Clear pages branch history
cd $DIR
PAGES_DIR=$PWD

git checkout --orphan gh-pages
rm -rf .gitignore
rm -rf .gitmodules
rm -rf *

# Create an aar and place it in the pages branch
cd $HOME_DIR
./gradlew -Dorg.gradle.project.repoDir="$PAGES_DIR" uploadArchives

# Commit pages branch
cd $PAGES_DIR
git add --all .
git commit -m "Updated at $(date)"

# Push to gh-pages
git push origin gh-pages

# Delete pages directory we used
cd ..
rm -rf $DIR