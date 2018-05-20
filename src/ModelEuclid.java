import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ModelEuclid {
	private BufferedReader reader;
	private String posDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\poswords.txt";
	private String negDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\negwords.txt";
	private ArrayList<String> posDic = new ArrayList<String>();
	private ArrayList<String> negDic = new ArrayList<String>();

	ArrayList<String[]> data;
	ArrayList<String[]> testdata;

	private int correctPrediction;
	private int allPrediction;

	double[] tpAvg = { 0, 0 }, dpAvg = { 0, 0 }, tnAvg = { 0, 0 }, dnAvg = { 0, 0 };
	double[] tpVar = { 0, 0 }, dpVar = { 0, 0 }, tnVar = { 0, 0 }, dnVar = { 0, 0 };

	public ModelEuclid() {
		structDictionaries();

		Reader reader = new Reader();
		reader.read("C:\\Users\\Ood\\Desktop\\MLTEMP\\TrainingSet.csv");
		data = reader.getData();

		reader = new Reader();
		reader.read("C:\\Users\\Ood\\Desktop\\MLTEMP\\TestingSet.csv");
		testdata = reader.getData();
	}

	private void structDictionaries() {
		String line = "";
		try {
			reader = new BufferedReader(new FileReader(posDicPath));
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				posDic.add(line);
			}
			reader = new BufferedReader(new FileReader(negDicPath));
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				negDic.add(line);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void printDic(ArrayList<String> dic) {
		for (int i = 0; i < dic.size(); i++) {
			System.out.println(dic.get(i));
		}
	}

	public void calculateParam() {
		double[] tpScore = { 0, 0 }, dpScore = { 0, 0 }, tnScore = { 0, 0 }, dnScore = { 0, 0 };
		int tpC = 0, dpC = 0, tnC = 0, dnC = 0;

		for (int i = 0; i < data.size(); i++) {
			String[] review = data.get(i)[4].split(" ");
			int posScore = 0, negScore = 0;
			for (int j = 0; j < review.length; j++) {
				for (int k = 0; k < posDic.size(); k++) {
					if (review[j].equals(posDic.get(k)))
						posScore++;
				}
				for (int k = 0; k < negDic.size(); k++) {
					if (review[j].equals(negDic.get(k)))
						negScore++;
				}
			}
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("positive")) {
				tpScore[0] += posScore;
				tpScore[1] += negScore;
				tpC++;
			}
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("positive")) {
				dpScore[0] += posScore;
				dpScore[1] += negScore;
				dpC++;
			}
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("negative")) {
				tnScore[0] += posScore;
				tnScore[1] += negScore;
				tnC++;
			}
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("negative")) {
				dnScore[0] += posScore;
				dnScore[1] += negScore;
				dnC++;
			}
		}
		tpAvg = getAvg(tpScore, tpC);
		dpAvg = getAvg(dpScore, dpC);
		tnAvg = getAvg(tnScore, tnC);
		dnAvg = getAvg(dnScore, dnC);
		getVar();

	}

	private double isItRight(String prediction, String deceptive, String polar) {
		if (deceptive.equals("truthful") && prediction.equals("tp"))
			return 1;
		if (deceptive.equals("deceptive") && prediction.equals("dp"))
			return 1;
		if (deceptive.equals("truthful") && prediction.equals("tn"))
			return 1;
		if (deceptive.equals("deceptive") && prediction.equals("dn"))
			return 1;
		return 0;
	}

	private double[] getAvg(double[] score, double count) {
		double[] result = { 0, 0 };
		result[0] = score[0] / count;
		result[1] = score[1] / count;
		return result;
	}

	private void getVar() {
		double[] tpCum = { 0, 0 }, dpCum = { 0, 0 }, tnCum = { 0, 0 }, dnCum = { 0, 0 };
		int tpC = 0, dpC = 0, tnC = 0, dnC = 0;
		for (int i = 0; i < data.size(); i++) {
			String[] review = data.get(i)[4].split(" ");
			int posScore = 0, negScore = 0;
			for (int j = 0; j < review.length; j++) {
				for (int k = 0; k < posDic.size(); k++) {
					if (review[j].equals(posDic.get(k)))
						posScore++;
				}
				for (int k = 0; k < negDic.size(); k++) {
					if (review[j].equals(negDic.get(k)))
						negScore++;
				}
			}
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("positive")) {
				tpCum[0] += Math.pow(posScore - tpAvg[0], 2);
				tpCum[1] += Math.pow(negScore - tpAvg[1], 2);
				tpC++;
			}
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("positive")) {
				dpCum[0] += Math.pow(posScore - dpAvg[0], 2);
				dpCum[1] += Math.pow(negScore - dpAvg[1], 2);
				dpC++;
			}
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("negative")) {
				tnCum[0] += Math.pow(posScore - tnAvg[0], 2);
				tnCum[1] += Math.pow(negScore - tnAvg[1], 2);
				tnC++;
			}
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("negative")) {
				dnCum[0] += Math.pow(posScore - dnAvg[0], 2);
				dnCum[1] += Math.pow(negScore - dnAvg[1], 2);
				dnC++;
			}
		}
		tpVar[0] = tpCum[0] / tpC;
		tpVar[1] = tpCum[1] / tpC;
		dpVar[0] = dpCum[0] / dpC;
		dpVar[1] = dpCum[1] / dpC;
		tnVar[0] = tnCum[0] / tnC;
		tnVar[1] = tnCum[1] / tnC;
		dnVar[0] = dnCum[0] / dnC;
		dnVar[1] = dnCum[1] / dnC;
	}

	public void evaluate() {
		double correctCount = 0, allCount = 0;
		double tpCall = 0, dpCall = 0, tnCall = 0, dnCall = 0;
		double tpCorrect = 0, dpCorrect = 0, tnCorrect = 0, dnCorrect = 0;
		double tpCount = 0, dpCount = 0, tnCount = 0, dnCount =0;
		for (int i = 0; i < testdata.size(); i++) {
			String[] review = testdata.get(i)[4].split(" ");
			double posScore = 0, negScore = 0;
			String prediction = "";

			for (int j = 0; j < review.length; j++) {
				for (int k = 0; k < posDic.size(); k++) {
					if (review[j].equals(posDic.get(k)))
						posScore++;
				}
				for (int k = 0; k < negDic.size(); k++) {
					if (review[j].equals(negDic.get(k)))
						negScore++;
				}
			}

			double tpScore = calculateScore(posScore, negScore, tpAvg[0], tpAvg[1], tpVar[0], tpVar[1]);
			double dpScore = calculateScore(posScore, negScore, dpAvg[0], dpAvg[1], dpVar[0], dpVar[1]);
			double tnScore = calculateScore(posScore, negScore, tnAvg[0], tnAvg[1], tnVar[0], tnVar[1]);
			double dnScore = calculateScore(posScore, negScore, dnAvg[0], dnAvg[1], dnVar[0], dnVar[1]);

			double lowestScore = Math.min(Math.min(tpScore, dnScore), Math.min(tnScore, dpScore));

			if (lowestScore == tpScore) {
				prediction = "tp";
				tpCall++;
			} else if (lowestScore == dpScore) {
				prediction = "dp";
				dpCall++;
			} else if (lowestScore == tnScore) {
				prediction = "tn";
				tnCall++;
			} else {
				prediction = "dn";
				dnCall++;
			}

			correctCount += isItRight(prediction, testdata.get(i)[0], testdata.get(i)[2]);
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("positive"))
				tpCount++;
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("positive"))
				dpCount++;
			if (data.get(i)[0].equals("truthful") && data.get(i)[2].equals("negative"))
				tnCount++;
			if (data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("negative"))
				dnCount++;
			if (isItRight(prediction, testdata.get(i)[0], testdata.get(i)[2]) == 1)
				if (lowestScore == tpScore) {
					prediction = "tp";
					tpCorrect++;
				} else if (lowestScore == dpScore) {
					prediction = "dp";
					dpCorrect++;
				} else if (lowestScore == tnScore) {
					prediction = "tn";
					tnCorrect++;
				} else {
					prediction = "dn";
					dnCorrect++;
				}

			allCount++;
		}
		System.out.println("Correct Count: " + correctCount);
		System.out.println("All Count: " + allCount);
		System.out.println("Accuracy: " + (correctCount / allCount));
		System.out.println("tpCorrect: "+ tpCorrect);
		System.out.println("TP recall: "+tpCorrect/tpCount);
		System.out.println("DP recall: "+dpCorrect/dpCount);
		System.out.println("TN recall: "+tnCorrect/tnCount);
		System.out.println("DN recall: "+dnCorrect/dnCount);

		System.out.println("TP call: "+tpCall);
		System.out.println("DP call: "+dpCall);
		System.out.println("TN call: "+tnCall);
		System.out.println("DN call: "+dnCall);

		System.out.println("TP count: "+tpCount);
		System.out.println("DP count: "+dpCount);
		System.out.println("TN count: "+tnCount);
		System.out.println("DN count: "+dnCount);

		System.out.println("TP precision: "+tpCorrect/tpCall);
		System.out.println("DP precision: "+dpCorrect/dpCall);
		System.out.println("TN precision: "+tnCorrect/tnCall);
		System.out.println("DN precision: "+dnCorrect/dnCall);
	}

	private double calculateScore(double x, double y, double xbar, double ybar, double xVar, double yVar) {
		double result = 0;
		//		xVar = Math.sqrt(xVar);
		//		yVar = Math.sqrt(yVar);
		double xPrime = xbar - xVar, xPrime2 = xbar + xVar;
		double yPrime = ybar - yVar, yPrime2 = ybar + yVar;
		result += Math.sqrt(Math.pow(x - xPrime, 2) + Math.pow(y - yPrime, 2));
		result += Math.sqrt(Math.pow(x - xPrime2, 2) + Math.pow(y - yPrime2, 2));
		//		result += Math.sqrt(Math.pow(x - xbar, 2) + Math.pow(y - ybar, 2));
		return result;
	}
}
