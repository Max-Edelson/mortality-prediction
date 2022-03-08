import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.IsotonicRegression;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.supervised.instance.StratifiedRemoveFolds;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;
import java.lang.Exception;
import java.util.Arrays;

public class LR {
	static double trainingTestingPercentSplit = 0.9;

	private static int deathCounter(Instances C) {
		int deathCounter = 0;

		for (int i = 0; i < C.numInstances(); i++) {
			if (C.get(i).classValue() == 1) {
				deathCounter++;
			}
		}

		return deathCounter;
	}

	private static double deathCounter(ArrayList<Instance> C) {
		double deathCounter = 0.0;

		for (int i = 0; i < C.size(); i++) {
			if (C.get(i).classValue() == 1) {
				deathCounter++;
			}
		}

		return deathCounter;
	}

	private static double[] obsDeaths(ArrayList<ArrayList<Instance>> C) {
		double[] deathArr = new double[C.size()];

		for (int i = 0; i < C.size(); i++) {
			deathArr[i] = deathCounter(C.get(i));
		}

		return deathArr;
	}

	private static int dischargeCounter(Instances C) {
		int dischargeCounter = 0;

		for (int i = 0; i < C.numInstances(); i++) {
			if (C.get(i).classValue() == 0) {
				dischargeCounter++;
			}
		}

		return dischargeCounter;
	}

	private static double dischargeCounter(ArrayList<Instance> C) {
		double dischargeCounter = 0.0;

		for (int i = 0; i < C.size(); i++) {
			if (C.get(i).classValue() == 0) {
				dischargeCounter++;
			}
		}

		return dischargeCounter;
	}

	private static double[] obsDischarged(ArrayList<ArrayList<Instance>> C) {
		double[] dischargeArr = new double[C.size()];

		for (int i = 0; i < C.size(); i++) {
			dischargeArr[i] = dischargeCounter(C.get(i));
		}

		return dischargeArr;
	}

	private static Instances[] transferInstances(Instances A, Instances B, int missingDeaths) {
		int deathsRemoved = 0;
		int dischargedRemoved = 0;
		ArrayList<Instance> removedDeaths = new ArrayList<Instance>();
		ArrayList<Instance> removedDischarged = new ArrayList<Instance>();
		Instances[] returnArr = new Instances[2];

		for (int i = 0; i < A.numInstances(); i++) {
			if (deathsRemoved == missingDeaths) {
				break;
			}

			if (A.get(i).classValue() == 1) {
				Instance removedInstance = A.get(i);
				removedDeaths.add(removedInstance);
				A.remove(i);
				deathsRemoved++;
			}
		}

		for (int i = 0; i < B.numInstances(); i++) {
			if (dischargedRemoved == missingDeaths) {
				break;
			}

			if (B.get(i).classValue() == 0) {
				Instance removedInstance = B.get(i);
				removedDischarged.add(removedInstance);
				B.remove(i);
				dischargedRemoved++;
			}
		}

		for (int i = 0; i < removedDeaths.size(); i++) {
			B.add(removedDeaths.get(i));
		}

		for (int i = 0; i < removedDischarged.size(); i++) {
			A.add(removedDischarged.get(i));
		}

		returnArr[0] = A;
		returnArr[1] = B;
		return returnArr;
	}

	private static Instances[] balanceInstances(Instances A, Instances B) {
		int ADeaths = deathCounter(A);
		int BDeaths = deathCounter(B);
		Instances[] returnArr = new Instances[2];

		boolean AHasMoreDeaths = ADeaths > BDeaths ? true : false;
		int missingDeaths = AHasMoreDeaths ? (ADeaths - BDeaths) : (BDeaths - ADeaths);

		if (AHasMoreDeaths) {
			returnArr = transferInstances(A, B, missingDeaths);
		} else if (!AHasMoreDeaths && missingDeaths > 0) {
			returnArr = transferInstances(B, A, missingDeaths);
		} else {
			returnArr[0] = A;
			returnArr[1] = B;
		}

		return returnArr;
	}

	private static int calculateBucket(double predictionScore, int numBuckets) {
		int bucket = -1;
		double sizePerBracket = 1.0 / (double) numBuckets;

		for (int i = 0; i < numBuckets; i++) {
			if ((predictionScore >= i * sizePerBracket) && (predictionScore < (i+1) * sizePerBracket)) {
				bucket = i;
			} else if (predictionScore == numBuckets * sizePerBracket) {
				bucket = numBuckets - 1;
			}
		}

		return bucket;
	}

	private static double[] appendHLPValue(double[] hlStatistics, double pValue) {
		double[] returnValue = new double[hlStatistics.length + 1];

		for (int i = 0; i < returnValue.length; i++) {
			if (i < hlStatistics.length) {
				returnValue[i] = hlStatistics[i];
			} else {
				returnValue[i] = pValue;
			}
		}

		return returnValue;
	}

	private static double sumPArrayList(ArrayList<Instance> instances, Attribute prediction) {
		double sum = 0;
		double summed[] = new double[instances.size()];

		for (Instance instance : instances) {
			sum += Double.valueOf(String.format("%.6f", instance.value(prediction)));
		}

		return sum;
	}

	private static double[] sumPDoubleArrayList(ArrayList<ArrayList<Instance>> instances, Attribute prediction) {
		double[] sumPArr = new double[instances.size()];

		for (int i = 0; i < instances.size(); i++)
			sumPArr[i] = sumPArrayList(instances.get(i), prediction);

		return sumPArr;
	}

	private static ArrayList<ArrayList<Instance>> split(Instances predictionDistribution, String statisticType, int numBuckets, Attribute prediction) {
		ArrayList<ArrayList<Instance>> split = new ArrayList<ArrayList<Instance>>();
		int i;
		int bucket = 0;
		double predictionScore;

		for (i = 0; i < numBuckets; i++)
			split.add(new ArrayList<Instance>());

		if (statisticType.equals("C")) {
			// same number of instances per bin
			int numPerBin = predictionDistribution.numInstances() / numBuckets;
			int leftOver = predictionDistribution.numInstances() % numBuckets;
			for (i = 0; i < predictionDistribution.numInstances(); i++) {
				predictionScore = Double.valueOf(String.format("%.6f", predictionDistribution.get(i).value(prediction)));
				if (split.get(bucket).size() >= numPerBin && bucket < numBuckets - 1)
					bucket++;
				split.get(bucket).add(predictionDistribution.get(i));
			}
		} else if (statisticType.equals("H")) {
			for (i = 0; i < predictionDistribution.numInstances(); i++) {
				predictionScore = Double.valueOf(String.format("%.6f", predictionDistribution.get(i).value(prediction)));
				bucket = calculateBucket(predictionScore, numBuckets);
				split.get(bucket).add(predictionDistribution.get(i));
			}
		} else {
			System.out.println("Illegal statisticType.");
			return new ArrayList<ArrayList<Instance>>();
		}

		return split;
	}

	private static double[] predictedProbabilities(double[] sumP1Arr, ArrayList<ArrayList<Instance>> instanceCollectionArr) {
		double[] predictedProb = new double[sumP1Arr.length];

		for (int i = 0; i < sumP1Arr.length; i++) {
			if (instanceCollectionArr.get(i).size() != 0) {
				predictedProb[i] = sumP1Arr[i] / instanceCollectionArr.get(i).size();
			} else {
				predictedProb[i] = 0.0;
			}
		}

		return predictedProb;
	}

	private static double[] expectedNumber(ArrayList<ArrayList<Instance>> instanceCollectionArr, double[] predictedProb, String target) {
		double[] exp = new double[instanceCollectionArr.size()];

		for (int i = 0; i < exp.length; i++) {
			if (target.equals("death")) {
				exp[i] = predictedProb[i] * instanceCollectionArr.get(i).size();
			} else if (target.equals("discharge")) {
				exp[i] = (1 - predictedProb[i]) * instanceCollectionArr.get(i).size();
			} else {
				System.out.println("Invalid target");
			}
		}

		return exp;
	}

	private static double[] hosmer_lemeshow_statistic(Instances predictionDistribution, String statisticType, int numBuckets, Attribute prediction, String classifier) {
		predictionDistribution.sort(prediction);
		ArrayList<ArrayList<Instance>> instanceCollectionArr = split(predictionDistribution, statisticType, numBuckets, prediction);

		double[] sumP1Arr = sumPDoubleArrayList(instanceCollectionArr, prediction);				// sum of predictions
		double[] predictedProb = predictedProbabilities(sumP1Arr, instanceCollectionArr);		// predicted probability for death
		double[] obs = obsDeaths(instanceCollectionArr); 										// observed deaths
		double[] obs_not = obsDischarged(instanceCollectionArr);								// observed discharged
		double[] exp = expectedNumber(instanceCollectionArr, predictedProb, "death");			// expected deaths
		double[] exp_not = expectedNumber(instanceCollectionArr, predictedProb, "discharge");	// expected discharged
		double degreesOfFreedom = (double) numBuckets - 2.0;
		double binSum = 0.0;
		double hStat = 0.0;
		int i;

		for (i = 0; i < numBuckets; i++) {
			if (exp[i] == 0 || exp_not[i] == 0) {
				continue;
			}

			binSum = Math.pow(obs[i] - exp[i], 2)/exp[i] + Math.pow(obs_not[i] - exp_not[i], 2)/exp_not[i];
			hStat += binSum;
		}

		ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreesOfFreedom);
		double pValue = 1 - chiSquaredDistribution.cumulativeProbability(hStat);

		return new double[]{hStat, pValue};
	}

	private static void printHLStatistic(double[] hlHStatistic, String classifier) {
		if (classifier.equals("isotonic regression")) {
			System.out.println("p-value:\t\t" + String.format("%.3f", hlHStatistic[1]));
		}
	}

	private static void performCalibration(Instances ninetyPercentData, Instances tenPercentData, int numRepetitions, String classifier, Logistic classifierObject, int folds, String size) throws Exception {
		boolean firstRun = true;
		double[] AUCResultsAfterCalibration = new double[numRepetitions];
		ArrayList<double[]> classifierCStatisticResults = new ArrayList<double[]>();
		ArrayList<double[]> classifierHStatisticResults = new ArrayList<double[]>();
		ArrayList<double[]> isoCStatisticResults = new ArrayList<double[]>();
		ArrayList<double[]> isoHStatisticResults = new ArrayList<double[]>();

		System.out.println("Beginning calibration");

		System.out.println();
		classifierObject.buildClassifier(ninetyPercentData);

		for (int i = 0; i < numRepetitions; i++) {
			tenPercentData.stratify(folds);
			int halfOfData = tenPercentData.numInstances() / 2;

			Instances trainingData = new Instances(tenPercentData, 0, halfOfData);
			Instances testingData = new Instances(tenPercentData, halfOfData, halfOfData);

			Instances[] balancedData = balanceInstances(trainingData, testingData);
			trainingData = balancedData[0];
			testingData = balancedData[1];

			String[] distributionForInstanceTrainingArr = new String[trainingData.numInstances()];
			String[] distributionForInstanceCalibratedArr= new String[trainingData.numInstances()];
			String currInstanceDistribution;
			double[] calibrationPredictionScores = new double[testingData.numInstances()];

			ArrayList<Attribute> attInfo = new ArrayList<Attribute>();
			Attribute outcome = new Attribute("outcome");
			Attribute prediction = new Attribute("prediction");
			attInfo.add(outcome);
			attInfo.add(prediction);

			Instances calibrationOneLabelData1 = new Instances("calibrationOneLabelData1", attInfo, trainingData.numInstances());
			calibrationOneLabelData1.setClass(outcome);
			Instances calibrationOneLabelData2 = new Instances("calibrationOneLabelData2", attInfo, testingData.numInstances());
			calibrationOneLabelData2.setClass(outcome);
			Instances isotonicRegressionPredictionValues = new Instances("isotonicRegressionPredictionValues", attInfo, testingData.numInstances());
			isotonicRegressionPredictionValues.setClass(outcome);

			ArrayList<Prediction> predictionsArr1 = new ArrayList<Prediction>();
			ArrayList<Prediction> predictionsArr2 = new ArrayList<Prediction>();

			NominalPrediction nominalPrediction;

			// seperate the classifierObject prediction scores and the labels
			// from the other features and put them in a new Instances
			// object (calibrationOneLabelData1). This is done by feeding
			// in the first 5% (trainingData).
			for (int j = 0; j < trainingData.numInstances(); j++) {
				double classValueOutcome = trainingData.get(j).classValue();
				double[] instanceDistribution = classifierObject.distributionForInstance(trainingData.get(j));

				double[] distributionOfPrediction = new double[2];
				distributionOfPrediction[0] = classValueOutcome;
        		distributionOfPrediction[1] = instanceDistribution[1];

				Instance inst = new DenseInstance(1, distributionOfPrediction);
				inst.setDataset(calibrationOneLabelData1);
				calibrationOneLabelData1.add(inst);

				nominalPrediction = new NominalPrediction(classValueOutcome, instanceDistribution);
				predictionsArr1.add(nominalPrediction);

				if (firstRun) {
					currInstanceDistribution = Double.toString(instanceDistribution[1]);
					distributionForInstanceTrainingArr[j] = currInstanceDistribution + "," + trainingData.get(j).classValue() + "\n";
				}
			}

			// seperate the classifierObject prediction scores and the labels
			// from the other features and put them in a new Instances
			// object (calibrationOneLabelData2). This is done by feeding
			// in the second 5% (testingData).
			for (int j = 0; j < testingData.numInstances(); j++) {
				double classValueOutcome = testingData.get(j).classValue();
				double[] instanceDistribution = classifierObject.distributionForInstance(testingData.get(j));

				double[] distributionOfPrediction = new double[2];
				distributionOfPrediction[0] = classValueOutcome;
				distributionOfPrediction[1] = instanceDistribution[1];

				Instance inst = new DenseInstance(1, distributionOfPrediction);
				inst.setDataset(calibrationOneLabelData2);
				calibrationOneLabelData2.add(inst);
            }

			// Make a new isotonicRegression object and train it on the
			// classifierObject prediction scores and label from the first 5%
			// (calibrationOneLabelData1).
			IsotonicRegression iso = new IsotonicRegression();
			iso.buildClassifier(calibrationOneLabelData1);

			for (int j = 0; j < calibrationOneLabelData2.numInstances(); j++) {
				double currentPrediction = iso.classifyInstance(calibrationOneLabelData2.get(j));
				if (firstRun) {
					currInstanceDistribution = Double.toString(currentPrediction);
					distributionForInstanceCalibratedArr[j] = currInstanceDistribution + "," + calibrationOneLabelData2.get(j).classValue() + "\n";
				}
				double classValueOutcome = testingData.get(j).classValue();
				double[] distributionOfPrediction = new double[2];
				double[] instanceDistribution = new double[]{ (1 - currentPrediction), currentPrediction };
				distributionOfPrediction[0] = classValueOutcome;
				distributionOfPrediction[1] = instanceDistribution[1];

				Instance inst = new DenseInstance(1, distributionOfPrediction);
				inst.setDataset(isotonicRegressionPredictionValues);
				isotonicRegressionPredictionValues.add(inst);

				nominalPrediction = new NominalPrediction(classValueOutcome, instanceDistribution);
				predictionsArr2.add(nominalPrediction);
			}

			ThresholdCurve tc = new ThresholdCurve();
			Instances resultAfterCalibration = tc.getCurve(predictionsArr2);
			double AUCAfterCalibration = Double.parseDouble(Utils.doubleToString(tc.getROCArea(resultAfterCalibration), 3));
			AUCResultsAfterCalibration[i] = AUCAfterCalibration;

			System.out.println("AUC after calibration:\t" + String.format("%.3f", AUCAfterCalibration));

			int numBucketsForHLStatistic = 10;
			isoHStatisticResults.add(hosmer_lemeshow_statistic(isotonicRegressionPredictionValues, "H", numBucketsForHLStatistic, prediction, "isotonic regression"));

			printHLStatistic(isoHStatisticResults.get(i), "isotonic regression");
		}
	}

	public static void runModels(int folds, String size, Instances trainingData, Instances testingData, String classifier, Logistic lr) throws Exception {
		System.out.println("Start Training Model");
		long startTrainingTime = System.currentTimeMillis();
		lr.buildClassifier(trainingData);
		long endTrainingTime = System.currentTimeMillis();

		long durationTraining = (endTrainingTime - startTrainingTime);

		System.out.println("Start Testing Model\n");
		long testingStartTime = System.currentTimeMillis();
		Evaluation testingPhaseEval = new Evaluation(trainingData);
		testingPhaseEval.evaluateModel(lr, testingData);
		long testingEndTime = System.currentTimeMillis();
		long durationTesting = (testingEndTime - testingStartTime);

		ThresholdCurve tc = new ThresholdCurve();
		int classIndex = 0;
		Instances result = tc.getCurve(testingPhaseEval.predictions(), classIndex);

		double testAUC = Double.parseDouble(Utils.doubleToString(tc.getROCArea(result), 4));

		System.out.println("Training took " + (durationTraining / 1000) + " seconds.");
		System.out.println("Testing took " + (durationTesting / 1000) + " seconds.\n");
	}

	private static Instances convertToNominal(Instances A) throws Exception {
		NumericToNominal convert = new NumericToNominal();
		String[] options = new String[2];
		options[0]="-R";
		options[1]="first";  //range of variables to make numeric

		convert.setOptions(options);
		convert.setInputFormat(A);

		A = Filter.useFilter(A, convert);

		return A;
	}

	public static void main(String[] args) throws Exception {
		// loads data and set class index
		String dataSource = Utils.getOption("t", args);
		Instances data = DataSource.read(dataSource);
		String clsIndex = Utils.getOption("c", args);
		if (clsIndex.length() == 0)
			clsIndex = "last";
		if (clsIndex.equals("first"))
			data.setClassIndex(0);
		else if (clsIndex.equals("last"))
			data.setClassIndex(data.numAttributes() - 1);
		else
			data.setClassIndex(Integer.parseInt(clsIndex) - 1);

		NumericToNominal convert = new NumericToNominal();
		String[] options = new String[2];
		options[0]="-R";
		options[1]="1";  //range of variables to make nominal

		convert.setOptions(options);
		convert.setInputFormat(data);
		data = Filter.useFilter(data, convert);

		// other options
		int seed  = Integer.parseInt(Utils.getOption("s", args));
		int folds = Integer.parseInt(Utils.getOption("x", args));
		String size = Utils.getOption("l", args);

		// randomize data
		Random rand = new Random(seed);
		data.randomize(rand);
		if (data.classAttribute().isNominal())
			data.stratify(folds);

		String[] filterOptions = new String[6];
		filterOptions[0] = "-N";                 // indicate we want to set the number of folds
		filterOptions[1] = Integer.toString(folds);  // split the data into random folds
		filterOptions[2] = "-F";                 // indicate we want to select a specific fold
		filterOptions[3] = Integer.toString(1);  // select the first fold
		filterOptions[4] = "-S";                 // indicate we want to set the random seed
		filterOptions[5] = Integer.toString(seed);

		StratifiedRemoveFolds strRmvFolds;

        int trainSize = (int) Math.round(data.numInstances() * trainingTestingPercentSplit);
        int testSize = data.numInstances() - trainSize;
        Instances trainingData = new Instances(data, 0, trainSize);
        Instances testingData = new Instances(data, trainSize, testSize);

		System.out.println("training (90%): " + trainingData.numInstances() + ", testing (10%): " + testingData.numInstances());
		System.out.println("Total instances: " + data.numInstances() + "\n");

		Logistic classifierObject = new Logistic();

		if (size.equals("sparse")) {
			classifierObject.setRidge(1000);
			System.out.println("Set hyper-parameters to size sparse");
		} else if (size.equals("dense")) {
			classifierObject.setRidge(100);
			System.out.println("Set hyper-parameters to size dense");
		} else {
			System.out.println("Invalid size entry");
			return;
		}

		String classifierName = "Logistic Regression";
		int numRepetitions = 1;

		runModels(folds, size, trainingData, testingData, classifierName, classifierObject);
		performCalibration(trainingData, testingData, numRepetitions, classifierName, classifierObject, folds, size);
	}
}
