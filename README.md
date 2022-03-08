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
    - For MacOS machines (may require **sudo** priviledges)
      - `brew install wget`
    - For Linux machines
      - `sudo apt install wget`

---

## Instructions For First-Time Setup
1. Clone the repository onto your local machine using one of the following methods:
   -  git using `git clone <GitHub-repository-release-link>`
   - wget using `wget https://github.com/Max-Edelson/mortality-prediction/archive/refs/heads/main.zip`
   - Manually download the zip straight from the GitHub repository using [this link](ttps://github.com/Max-Edelson/mortality-prediction). 
2. Run `bash setup.sh` to download all the required dependencies.
3. In a shell, from the `/mortality-prediction` directory, navigate into the `/src` directory.
   - `cd src`
4. Choose a classifier to run from the following:
   - AdaBoost (AB.sh)
   - Logistic Regression (LR.sh)
   - MultiLayer Perceptron (MLP.sh)
   - Naive Bayes (NB.sh)
   - Support Vector Machine (SVM.sh)
   - Random Forest (RF.sh)
5.  Run the selected classifier with `bash <classifier-abbreviation>.sh`
    -  Ex. Choose Naive Bayes via `bash NB.sh`
6.  Follow the prompts.

Disclaimer: AdaBoost and Multilayer Perceptron may require more time to run (around 1 hour depending on the computer hardware specification).

---

## Acknowledgement
The author T-TK was funded by the U.S. National Institutes of Health (NIH) (R00HG009680, R01HL136835, R01GM118609, R01HG011066, U24LM013755). The content is solely the responsibility of the author and does not necessarily represent the official views of the NIH. The funders had no role in study design, data collection and analysis, decision to publish, or preparation of the manuscript.

---

## Contact
Thank you for downloading and using our software. If you have any questions or suggestions, please contact Maxim Edelson (medelson@ucsd.edu), UCSD Computer Science and Engineering, University of California San Diego, La Jolla, USA.
