#!/bin/bash
rm -rf images
mkdir images
image_file=$1
for image in $(cat $image_file)
do
	stub=$(echo $image | awk 'BEGIN{FS="|"};{print $1}')
	url=$(echo $image | awk 'BEGIN{FS="|"};{print $2}')
	echo "Downloading "$stub
	curl -s $url > "images/"$stub
done
