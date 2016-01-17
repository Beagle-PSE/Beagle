#!/bin/bash

##############################################################
# SVN deployment script                                      #
# Deploys Beagle’s code an built artefacts to the SDQ’s SVN. #
# To be called from Beagle’s root folder on Travis CI        #
#                                                            #
# author: Joshua Gleitze                                     #
##############################################################

# fail the script if any command fails
set -e

if [ "$TRAVIS_REPO_SLUG" != "Beagle-PSE/Beagle" ]; then
	echo "We won't update the SVN because this is not the main repository."
	exit 0
fi

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
	echo "We won't update the SVN because this is a pull request."
	exit 0
fi

if [ "$TRAVIS_BRANCH" != "master" ]; then
	echo "We won't update the SVN because this is not the master branch."
	exit 0
fi

if [ -z ${SVN_USER:+1} ]; then
	echo "We can't update the SVN because no SVN_USER was provided."
	exit 0
fi

if [ -z ${SVN_PASSWORD:+1} ]; then
	echo "We can't update the SVN because no SVN_PASSWORD was provided."
	exit 0
fi

SVN_URL="https://svnserver.informatik.kit.edu/i43/svn/stud/2015WS-PSE-Gruppe5/"


# Set up our environment
SVN=../svn
BASE=$PWD
rm -rf $SVN
mkdir -p $SVN

cd $SVN

# Check out from SVN
svn checkout $SVN_URL $SVN --username $SVN_USER --password "$SVN_PASSWORD" --non-interactive 

# Remove all content - except the svn files
find . -maxdepth 1 \! \( -name .svn -o -name . \) -exec rm -rf {} \;

# Copy in the files
cp -r "$BASE"/* .

# Remove files that are not needed
rm -rf .git **/.gradle .gradle

# Add all new and changed files to the index
svn add . --force --non-interactive

# Remove all removed files from the index (source: http://stackoverflow.com/questions/11066296/svn-delete-removed-files)
svn st | grep '^!' | awk '{$1=""; print " --force \""substr($0,2)"@\"" }' | xargs svn delete

svn commit -m "Automatic Travis update from https://github.com/Beagle-PSE/Beagle.git" --username $SVN_USER --password "$SVN_PASSWORD" --non-interactive

cd $BASE





