# Generalizable Prediction of COVID-19 Mortality on Worldwide Patient Data

## Citation
Please cite as below:

Citation pending...

---

## Authors:
Maxim Edelson<sup>1</sup>, Tsung-Ting Kuo<sup>2</sup> <br>
<sup>1</sup>UCSD Department of Computer Science and Engineering, University of California San Diego, La Jolla, CA, USA <br>
<sup>2</sup>UCSD Health Department of Biomedical Informatics, University of California San Diego, La Jolla, CA, USA

---

## Introduction
This code uses open-source data (found [here](https://github.com/beoutbreakprepared/nCoV2019)) to model patient mortality based on 55 features + 1 class. Predicting COVID-19 mortality for patients is critical for early-stage care and intervention. Existing studies mainly built models on datasets with limited geographical range (e.g., from a city) or size (e.g., ≤ 2,000 patients). In this study, we developed COVID-19 mortality prediction models on worldwide, large-scale “sparse” data (i.e., ~100,000 patients) and on a “dense” subset of the data (i.e., ~7000 patients with requirements of containing their age, sex, symptoms, chronic diseases, or optional dates). 

---

## Example Data
Example data can be found [here](data/example.csv).

---

## Prerequisites
  - Java 8 (Other version will not work)
    - Instructions for the download can be found [here](https://stackoverflow.com/questions/24342886/how-to-install-java-8-on-mac).
    - If running an older version of java, instructions to switch to java 8 can be found [here](https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-macos).

---

## Instructions For First-Time Setup
1. Cloan repository
2. Download commons-math3-3.6.1-bin.tar.gz from https://commons.apache.org/proper/commons-math/download_math.cgi. Do not download the .zip (it will not work).
3. Download LibLINEAR's dependencies: https://mvnrepository.com/artifact/nz.ac.waikato.cms.weka/LibLINEAR/1.9.7, http://www.java2s.com/Code/Jar/l/Downloadliblinear192jar.htm.
4. Download Weka's dependencies: https://prdownloads.sourceforge.net/weka/weka-3-9-6.zip.
5. Download Isotonic Regression's dependencies (1.0.2.jar): https://jarcasting.com/artifacts/nz.ac.waikato.cms.weka/isotonicRegression/.
6. Place all the downloaded and unzipped directories/jar files into the `/lib` directory (do **NOT** extract the .jar files out).
7. Run `bash setup.sh` to fix dependencies
8. In a shell, `cd` into the `\src` directory.
9. Choose a classifier to run
   1. AdaBoost (AB.sh)
   2. Logistic Regression (LR.sh)
   3. MultiLayer Perceptron (MLP.sh)
   4. Naive Bayes (NB.sh)
   5. Support Vector Machine (SVM.sh)
   6. Random Forest (RF.sh)
10. Run the classifier with `bash classifier-abbreviation.sh`
    1.  Ex. `bash AB.sh`

---

## Acknowledgement
The author T-TK was funded by the U.S. National Institutes of Health (NIH) (R00HG009680, R01HL136835, R01GM118609, R01HG011066, U24LM013755). The content is solely the responsibility of the author and does not necessarily represent the official views of the NIH. The funders had no role in study design, data collection and analysis, decision to publish, or preparation of the manuscript.

---

## Contact
Thank you for downloading and using our software. If you have any questions or suggestions, please contact Maxim Edelson (medelson@ucsd.edu), UCSD Computer Science and Engineering, University of California San Diego, La Joll,a USA.

---

## DOI