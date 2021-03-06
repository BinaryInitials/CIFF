#!/bin/bash


directories=`ls`
jarName="ciff-website.jar"
mainClass="com.ozone.ciff.Run2"

src="src"
bin="bin"
libs="libs"
if [[ " ${directories[@]} " =~ $libs ]]; then
	echo "Including Libraries..."
	libraries_for_compiler=`echo $(ls libs/) | perl -pe 's/jar/jar:/g' | perl -pe 's/ //g' | perl -pe 's/:$//g' | perl -pe 's/:/:libs\//g' | perl -pe 's/^/libs\//g'`
	libraries_for_manifest=`echo $(ls libs/)`
	echo $libraries_for_compiler
	echo $libraries_for_manifest
	if [[ " ${directories[@]} " =~ $bin ]]; then
		echo "Recompiling..."
		rm -r $bin/
	else 
		echo "Compiling..."
	fi

	if [[ " ${directories[@]} " =~ $src ]]; then
		mkdir bin && javac -XDignore.symbol.file -cp $libraries_for_compiler -d bin $(find . -name "*.java"  | grep -v Test) && echo "Class-Path: " > manifest.txt && for i in $libraries_for_manifest; do echo " libs/"$i" " >> manifest.txt ; done && echo "Main-Class: "$mainClass >> manifest.txt && cd bin && jar -cvfm ../$jarName ../manifest.txt *
		echo "Build succeeded. To run, use: java -jar "$jarName
	else
		echo "build-script is not in a java project. Please move the script directly in a java project."
		exit 1
	fi
	exit 1
fi

if [[ " ${directories[@]} " =~ $bin ]]; then
	echo "Recompiling..."
	rm -r $bin/
else 
	echo "Compiling..."
fi

if [[ " ${directories[@]} " =~ $src ]]; then
	mkdir bin && javac -XDignore.symbol.file -d bin $(find . -name "*.java"  | grep -v Test) && cd bin && echo "Main-Class: "$mainClass > manifest.txt && jar -cvfm $jarName manifest.txt * && mv $jarName ../
	echo "Build succeeded. To run, use: java -jar "$jarName
else
	echo "build-script is not in a java project. Please move the script directly in a java project."
	exit 1
fi
