import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ModelTuner {
	private BufferedReader reader;
	private String posDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\pos-dict.txt";
	private String negDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\negwords.txt";
	private ArrayList<String> posDic = new ArrayList<String>();
	private ArrayList<String> negDic = new ArrayList<String>();

	ArrayList<String[]> data;

	public ModelTuner() {
		structDictionaries();

		Reader testingSetReader = new Reader();
		testingSetReader.read("C:\\Users\\Ood\\Desktop\\MLTEMP\\TrainingSet.csv");
		data = testingSetReader.getData();
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

	public void evaluate() {

		double[] tpScore = { 0, 0 }, dpScore = { 0, 0 }, tnScore = { 0, 0 }, dnScore = { 0, 0 };
		int tpC = 0, dpC = 0, tnC = 0, dnC = 0;

		for (int i = 0; i < data.size(); i++) {
			String[] review = data.get(i)[4].split(" ");
			int posScore = 0, negScore = 0;
			for (int j = 0; j < review.length; j++) {
				for (int k = 0; k < posDic.size(); k++) {
					if (review[j].equals(posDic.get(k))) {
						posScore++;
					}
						
				}
				for (int k = 0; k < negDic.size(); k++) {
					if (review[j].equals(negDic.get(k))) {
						negScore++;
					}
						
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
		double TPparam = (tpScore[0]) / (tpScore[0] + tpScore[1]);
		double DPparam = (dpScore[0]) / (dpScore[0] + dpScore[1]);
		double TNparam = (tnScore[0]) / (tnScore[0] + tnScore[1]);
		double DNparam = (dnScore[0]) / (dnScore[0] + dnScore[1]);
//		System.out.println("TP: " + TPparam);
//		System.out.println(tpScore[0] / tpC + " " + tpScore[1] / tpC);
//		System.out.println("DP: " + DPparam);
//		System.out.println(dpScore[0] / dpC + " " + dpScore[1] / dpC);
//		System.out.println("TN: " + TNparam);
//		System.out.println(tnScore[0] / tnC + " " + tnScore[1] / tnC);
//		System.out.println("DN: " + DNparam);
//		System.out.println(dnScore[0] / dnC + " " + dnScore[1] / dnC);
	}
}
