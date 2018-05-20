import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Model {
	private BufferedReader reader;
	private String posDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\pos-dict.txt";
	private String negDicPath = "C:\\Users\\Ood\\Desktop\\MLTEMP\\negwords.txt";
	private ArrayList<String> posDic = new ArrayList<String>();
	private ArrayList<String> negDic = new ArrayList<String>();

	ArrayList<String[]> data;
	
	private int correctPrediction;
	private int allPrediction;
	
	public Model() {
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
		for(int i=0;i<dic.size();i++) {
			System.out.println(dic.get(i));
		}
	}
	public void evaluate() {
		double correctCount = 0, allCount = 0;
		
		for (int i = 0;i<data.size();i++) {
			String[] review = data.get(i)[4].split(" ");
			double posScore = 0, negScore = 0;
			String prediction = "";
			
			for (int j = 0;j<review.length;j++) {
				for (int k =0;k<posDic.size();k++) {
					if(review[j].equals(posDic.get(k))) {
						posScore++;
					}
				}
				for (int k =0;k<negDic.size();k++) {
					if(review[j].equals(negDic.get(k))) {
						negScore++;
					}
				}
			}
			
			double score = posScore/(posScore+negScore);
			System.out.println(score);
			if(score >=0.9218163195629908) {
				prediction = "dp";
			} else if (score >= 0.8787781350482315) {
				prediction = "tp";
			} else if (score >= 0.5265540132770067) {
				prediction = "tn";
			} else {
				prediction = "dn";
			}
			correctCount+=isItRight(prediction, data.get(i)[0], data.get(i)[2]);
			allCount++;
		}
//		System.out.println();
//		System.out.println("Correct Count: "+correctCount);
//		System.out.println("All Count: "+allCount);
//		System.out.println("Accuracy: "+(correctCount/allCount));
	}
	
	private double isItRight(String prediction, String deceptive, String polar) {
		if (deceptive.equals("truthful") && prediction.equals("tp")) return 1;
		if (deceptive.equals("deceptive") && prediction.equals("dp")) return 1;
		if (deceptive.equals("truthful")  && prediction.equals("tn")) return 1;
		if (deceptive.equals("deceptive")  && prediction.equals("dn")) return 1;
		return 0;
	}
}
