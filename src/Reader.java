import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
	static ArrayList<String[]> data;
	Preprocessor pp;
	public Reader() {
        data = new ArrayList<String[]>();
        pp = new Preprocessor();
	}
	public void read(String path) {
		String csvFile = path;
        BufferedReader br = null;
        String line = "";
		try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] att = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                att[4] = preprocess(att[4]);
                data.add(att);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	private String preprocess(String att) {
		att = pp.toLowerCase(att);
        att = pp.replace(att, ",","");
        att = pp.replace(att, ".","");
        att = pp.replace(att, "\"","");
        att = pp.replace(att, "\'","");
        att = pp.replace(att, "(","");
        att = pp.replace(att, ")","");
//        att = pp.replace(att, "!","");
//        att = pp.replace(att, "?","");
        return att;
	}
	public ArrayList<String[]> getData() {
		return this.data;
	}
	
}
