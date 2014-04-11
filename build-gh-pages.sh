#!/bin/bash

HOME_DIR=$PWD
DIR=$(basename $PWD)-pages
URL=$(git config --get remote.origin.url)

# Delete local branch pages's directory
cd ..
rm -rf $DIR

# Check out pages branch
git clone $URL $DIR

# Refresh pages branch
cd $DIR
PAGES_DIR=$PWD

git checkout -b gh-pages
rm -rf .gitignore
rm -rf .gitmodules
rm -rf *

# Create an aar and place it in the pages branch
cd $HOME_DIR
./gradlew -Dorg.gradle.project.repoDir="$PAGES_DIR" uploadArchives
#./gradlew uploadArchives

# Commit pages branch
git add --all .
git commit -m "Updated at $(date)"

# Push to gh-pages
git push origin gh-pages

# Delete pages directory we used
cd ..
rm -rf $DIR