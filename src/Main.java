import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Reader reader = new Reader();
		reader.read("C:\\Users\\Ood\\Desktop\\MLTEMP\\TrainingSet.csv");
		ArrayList<String[]> data = reader.getData();
		//showAvgLength(data);
		
		ModelTuner mt = new ModelTuner();
		mt.evaluate();
		
		Model m = new Model();
		m.evaluate();
		
//		ModelEuclid me = new ModelEuclid();
//		me.calculateParam();
//		me.evaluate();
	}
	
	public static void showAvgLength(ArrayList<String[]> data) {
		double truthW = 0 , decW = 0 , posW = 0 , negW = 0;
		int truthC = 0 , decC = 0 , posC = 0 , negC = 0;
		double tpW = 0 , tnW = 0 , dpW = 0 , dnW = 0;
		int tpC = 0 , tnC = 0 , dpC = 0 , dnC = 0;
		
		for(int i=0;i<data.size();i++) {
			if(data.get(i)[0].equals("deceptive")) {
				decW += data.get(i)[4].split(" ").length;
				decC++;
			}
			if(data.get(i)[0].equals("truthful")) {
				truthW += data.get(i)[4].split(" ").length;
				truthC++;
			}
			if(data.get(i)[2].equals("positive")) {
				posW += data.get(i)[4].split(" ").length;
				posC++;
			}
			if(data.get(i)[2].equals("negative")) {
				negW += data.get(i)[4].split(" ").length;
				negC++;
			}
			
			if(data.get(i)[0].equals("truthful") && data.get(i)[2].equals("positive")) {
				tpW += data.get(i)[4].split(" ").length;
				tpC++;
			}
			if(data.get(i)[0].equals("truthful") && data.get(i)[2].equals("negative")) {
				tnW += data.get(i)[4].split(" ").length;
				tnC++;
			}
			if(data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("positive")) {
				dpW += data.get(i)[4].split(" ").length;
				dpC++;
			}
			if(data.get(i)[0].equals("deceptive") && data.get(i)[2].equals("negative")) {
				dnW += data.get(i)[4].split(" ").length;
				dnC++;
			}
			
		}
		System.out.println("Truthful:  "+truthW/truthC);
		System.out.println("Deceptive: "+decW/decC);
		System.out.println("Positive:  "+posW/posC);
		System.out.println("Negative:  "+negW/negC);

		System.out.println("TP:        "+tpW/tpC);
		System.out.println("TN:        "+tnW/tnC);
		System.out.println("DP:        "+dpW/dpC);
		System.out.println("DN:        "+dnW/dnC);
	}
}
