#!/bin/bash


cd ..
rm -rf images
mkdir images

for file_name in $(ls *_stub.txt); do
	image_url=$(cat $file_name | egrep "property.*https://www.clevelandfilm.org/files/films/" | perl -pe 's/.*content="([^"]+)".*/\1/g')
	stub=$(echo $file_name | perl -pe 's/_stub.*//g')
	curl -s $image_url > images/$stub
done