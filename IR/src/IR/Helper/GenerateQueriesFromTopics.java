package IR.Helper;
import java.io.*;
import java.util.*;

public class GenerateQueriesFromTopics {

    private static String TOPIC_PATH = "./Resource/topics.mdlp";
    ArrayList<QueryFieldsObject> queryFieldsObjectArray = new ArrayList<>();
    
    public void generateQueriesFromTopic() throws IOException {

    	QueryFieldsObject queryFieldsObject;
        InputStream readIn = new FileInputStream(new File(TOPIC_PATH));
        BufferedReader br = new BufferedReader(new InputStreamReader(readIn));
        String newline = "";
        String content = "";
        newline = br.readLine();
        while(newline != null){
        	
        	queryFieldsObject = new QueryFieldsObject();
        	
            if (newline.matches("<top>")){
                newline = br.readLine();

                while (!newline.matches("</top>")){

                    content = "";

                    if(newline.trim().matches("<num>\\sNumber:\\s\\d+")){
                        queryFieldsObject.num = newline.substring(14,17);
                    }else if (newline.trim().matches("^<title>[\\s\\S]+")){

                        queryFieldsObject.title = newline.substring(8);

                    }else if (newline.trim().matches("^<desc>[\\s\\S]+")) {
                        newline = br.readLine();
                        while(newline.length() != 0){
                            content = content + newline+" ";
                            newline = br.readLine();
                        }

                        queryFieldsObject.description = content;
                    }else if (newline.trim().matches("^<narr>[\\s\\S]+")){
                        newline = br.readLine();
                        while(newline.length() != 0){
                            content = content + newline+" ";
                            newline = br.readLine();
                        }
                        queryFieldsObject.narrative = content;
                    }
                    newline = br.readLine();
                }
                newline = br.readLine();
                newline = br.readLine();
            }
            queryFieldsObjectArray.add(queryFieldsObject);
        }

        br.close();
    }

	public ArrayList<QueryFieldsObject> getQueries() {
		return this.queryFieldsObjectArray;
	}
}
