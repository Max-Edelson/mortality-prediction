#!/bin/bash

cd lib

if [[ ! -d "weka-3-9-6" ]]; then
    echo "Missing weka-3-9-6 directory"
    exit
fi

if [[ ! -f "commons-math3-3.6.1-bin.tar" && ! -f "commons-math3-3.6.1-bin.tar.gz" ]]; then
    echo "Missing commons-math3-3.6.1-bin.tar"
    exit
fi

if [[ ! -f isotonicRegression-1.0.2.jar ]]; then
    echo "Missing isotonicRegression-1.0.2.jar"
    exit
fi

if [[ ! -f "LibLINEAR-1.9.7.jar" ]]; then
    echo "Missing LibLINEAR-1.9.7.jar"
    exit
fi

if [[ ! -f "liblinear-1.92.jar" ]]; then
    echo "Missing liblinear-1.92.jar"
    exit
fi

mv weka-3-9-6/weka.jar .
gunzip -c *.tar.gz | tar xopf -
tar -xf *.tar
rm -rf weka-3-9-6 commons-math3-3.6.1-bin.tar
mv isotonicRegression-1.0.2.jar isotonicRegression.jar

cd ..
