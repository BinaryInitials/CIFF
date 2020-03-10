#!/bin/bash

path_to_website=$1

./build-script-part1.sh
./build-script-part2.sh
./build-script-part3.sh

java -jar ciff-parser.jar
java -jar ciff-website.jar
cd scripts/
./image-downloader.sh
cd ..
java -jar ciff-image-shrinker.jar images

cp -r css $path_to_website
cp -r js $path_to_website
cp logo.png $path_to_website
cp icon.ico $path_to_website
cp index.html $path_to_website
cp images/ $path_to_website