#!/bin/bash

if [[ ! -d lib ]]; then
    mkdir lib
fi

cd lib

echo "Downloading missing dependencies."

if [[ ! -d "weka-3-9-6" ]]; then
    wget https://prdownloads.sourceforge.net/weka/weka-3-9-6.zip
fi

if [[ ! -f "commons-math3-3.6.1-bin.tar" && ! -f "commons-math3-3.6.1-bin.tar.gz" ]]; then
    wget https://dlcdn.apache.org//commons/math/binaries/commons-math3-3.6.1-bin.tar.gz
fi

if [[ ! -f isotonicRegression-1.0.2.jar ]]; then
    wget https://repo1.maven.org/maven2/nz/ac/waikato/cms/weka/isotonicRegression/1.0.2/isotonicRegression-1.0.2.jar
fi

if [[ ! -f "LibLINEAR-1.9.7.jar" ]]; then
    wget https://repo1.maven.org/maven2/nz/ac/waikato/cms/weka/LibLINEAR/1.9.7/LibLINEAR-1.9.7.jar
fi

if [[ ! -f "liblinear-1.92.jar" ]]; then
    wget http://www.java2s.com/Code/JarDownload/liblinear/liblinear-1.92.jar.zip
fi

unzip "*.zip"
mv weka-3-9-6/weka.jar .
gunzip -c *.tar.gz | tar xopf - > /dev/null 2>&1
tar -xf *.tar > /dev/null 2>&1
rm -rf weka-3-9-6 commons-math3-3.6.1-bin.tar *.zip *.tar *.gz
mv isotonicRegression-1.0.2.jar isotonicRegression.jar

cd ..

if [[ ! -d bin ]]; then
    mkdir bin
fi
