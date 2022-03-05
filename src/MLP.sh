#!/bin/bash

function sizePrompt {
        echo "Choose a dataset size below (enter 1, 2, or [q]uit):"
        echo "1) sparse"
        echo "2) dense"
        echo "q) quit"
}

function temporalPrompt {
        echo "Choose a dataset type below (enter 1, 2, 3, or [q]uit):"
        echo "1) Training + Calibration + Testing on Full Evaluation Data"
        echo "2) Training + Calibration + Testing on Evaluation Data w/ confirmation date before 5/2"
        echo "3) Training + Calibration + Testing on Evaluation Data w/ confirmation date after/on 5/2"
        echo "q) quit"
}

function checkQuit {
	if [ $1 == "q" ]
	then
		exit
	fi
}

sizePrompt
read size
until [[ $size == "1" || $size == "2" || $size == "q" ]]
do
	echo "Invalid input, try again."
	sizePrompt
	read size
done

checkQuit $size

temporalPrompt
read temporal
until [[ $temporal == "1" || $temporal == "2" || $temporal == "3" || $temporal == "q" ]]
do
	echo "Invalid input, try again."
	temporalPrompt
	read temporal
done

checkQuit $temporal
echo

case $size in
	1)
	size="sparse";;

	2)
	size="dense";;

	*)
	echo "Error occured. Exiting."
	exit;;
esac

case $temporal in
	1)
	temporal="";;

	2)
	temporal="_pre_temporal_evaluation";;

	3)
	temporal="_post_temporal_evaluation";;

	*)
	echo "Error occured. Exiting."
	exit;;
esac

if javac -d ../bin -cp ":../lib/weka.jar:../lib/isotonicRegression.jar:../lib/commons-math3-3.6.1/*" MLP.java
then
	cd ../bin
	if java -cp ":../lib/weka.jar" TestDataLoading 2>&1 | grep -q 'InaccessibleObjectException'
	then
		java --add-opens java.base/java.lang=ALL-UNNAMED -cp ":../lib/weka.jar:../lib/isotonicRegression.jar:../lib/commons-math3-3.6.1/*" MLP -t ../data/"$size$temporal".csv -c first -x 10 -s 1 -l "$size"
	else
		java -cp ":../lib/weka.jar:../lib/isotonicRegression.jar:../lib/commons-math3-3.6.1/*" MLP -t ../data/"$size$temporal".csv -c first -x 10 -s 1 -l "$size"
	fi
	cd ../src
else
	echo "Failed for some reason. Exiting..."
	exit
fi
