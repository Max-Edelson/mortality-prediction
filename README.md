# Generalizable Prediction of COVID-19 Mortality on Worldwide Patient Data

## Authors:
Maxim Edelson<sup>1</sup>, Tsung-Ting Kuo<sup>2</sup> <br>
<sup>1</sup>UCSD Department of Computer Science and Engineering, University of California San Diego, La Jolla, CA, USA <br>
<sup>2</sup>UCSD Health Department of Biomedical Informatics, University of California San Diego, La Jolla, CA, USA

---

## Introduction
This code uses publicly-available data (found [here](https://github.com/beoutbreakprepared/nCoV2019)) to model patient mortality based on 55 features + 1 class.

---

## Prerequisites
  - MacOS or a Linux system
  - Java 8 (Other versions will not work)
    - Instructions for the download can be found [here](https://stackoverflow.com/questions/24342886/how-to-install-java-8-on-mac).
    - If running an older version of java, instructions to switch to java 8 can be found [here](https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-macos).
  - wget
    - For MacOS (may require **sudo** priviledges)
      - `brew install wget`
    - For Linux machines
      - `sudo apt install wget`

---

## Instructions For First-Time Setup
1. Clone the repository onto your local machine using one of the following methods:
   -  git via `git clone <GitHub-repository-release-link>`
   - wget via `wget https://github.com/Max-Edelson/mortality-prediction/archive/refs/heads/main.zip`
   - Manually download the zip straight from the github using [this link](ttps://github.com/Max-Edelson/mortality-prediction). 
2. Run `bash setup.sh` to download all required dependencies.
3. In a shell, `cd` into the `src` directory.
4. Choose a classifier to run
   1. AdaBoost (AB.sh)
   2. Logistic Regression (LR.sh)
   3. MultiLayer Perceptron (MLP.sh)
   4. Naive Bayes (NB.sh)
   5. Support Vector Machine (SVM.sh)
   6. Random Forest (RF.sh)
5.  Run the classifier with `bash <classifier-abbreviation>.sh`
    1.  Ex. Choose Naive Bayes via `bash NB.sh`
6.  Follow the prompts.

Disclamer: AdaBoost, Multilayer Perceptron, and Random Forest may require more time to run (> 1 minute).

---

## Acknowledgement
The author T-TK was funded by the U.S. National Institutes of Health (NIH) (R00HG009680, R01HL136835, R01GM118609, R01HG011066, U24LM013755). The content is solely the responsibility of the author and does not necessarily represent the official views of the NIH. The funders had no role in study design, data collection and analysis, decision to publish, or preparation of the manuscript.

---

## Contact
Thank you for downloading and using our software. If you have any questions or suggestions, please contact Maxim Edelson (medelson@ucsd.edu), UCSD Computer Science and Engineering, University of California San Diego, La Jolla, USA.
