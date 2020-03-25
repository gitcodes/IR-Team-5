package IR.Helper;


import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GenerateQueriesFromTopics {

    private static String TOPIC_PATH = "./Resource/topics";
    private static String QUERYFILES = "./queries/";

    public static void main(String args[]) throws IOException {

        InputStream readIn = new FileInputStream(new File(TOPIC_PATH));

        BufferedReader br = new BufferedReader(new InputStreamReader(readIn));

        BufferedWriter bw1 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version1.txt"));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version2.txt"));
        BufferedWriter bw3 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version3.txt"));
        BufferedWriter bw4 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version4.txt"));
        BufferedWriter bw5 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version5.txt"));
        BufferedWriter bw6 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version6.txt"));
        BufferedWriter bw7 = new BufferedWriter(new FileWriter(QUERYFILES + "queries_version7.txt"));




        String newline = "";
        String keyName = "";
        String content = "";


        HashMap<String, String> topic = new HashMap<>();



        newline = br.readLine();
        //System.out.println(newline);

        while(newline != null){

            if (newline.matches("<top>")){
                //a new topic starts

                topic = new HashMap<>();

                newline = br.readLine();

                while (!newline.matches("</top>")){
                    //not reaching the end of the current topic

                    keyName = "";
                    content = "";

                    if(newline.trim().matches("<num>\\sNumber:\\s\\d+")){
                        //read number of the topic
                        keyName = "Number";
                        content = newline.substring(14,17);
                        //
                    }else if (newline.trim().matches("^<title>[\\s\\S]+")){

                        keyName = "Title";
                        content = newline.substring(8).trim();

                    }else if (newline.trim().matches("^<desc>[\\s\\S]+")) {
                        keyName = "Description";
                        newline = br.readLine();
                        while(newline.length() != 0){
                            //System.out.println("content:"+content+"  newline:"+newline);
                            content = content + newline.trim();
                            newline = br.readLine();
                        }

                    }else if (newline.trim().matches("^<narr>[\\s\\S]+")){
                        keyName = "Narrative";
                        newline = br.readLine();
                        while(newline.length() != 0){
                            content = content + newline.trim();
                            newline = br.readLine();
                        }

                    }


                    if (keyName != "" && content != ""){
                        topic.put(keyName,content);
                    }


                    newline = br.readLine();
                }

                //finish reading one topic
                newline = br.readLine();
                newline = br.readLine();
            }

            System.out.println(topic);


            //generate queries


            //v1 using Title

            bw1.write("Number:"+topic.get("Number"));
            bw1.write("\nContent:" + topic.get("Title")+"\n");
            bw1.newLine();


            //v2 using Description

            bw2.write("Number:"+topic.get("Number"));
            bw2.write("\nContent:" + topic.get("Description")+"\n");
            bw2.newLine();

            //v3 using Narrative

            bw3.write("Number:"+topic.get("Number"));
            bw3.write("\nContent:" + topic.get("Narrative")+"\n");
            bw3.newLine();

            //v4 using Title and Description

            bw4.write("Number:"+topic.get("Number"));
            bw4.write("\nContent:" + topic.get("Title")+topic.get("Description")+"\n");
            bw4.newLine();

            //5 using Title and Narrative

            bw5.write("Number:"+topic.get("Number"));
            bw5.write("\nContent:" + topic.get("Title")+topic.get("Narrative")+"\n");
            bw5.newLine();

            //6 using Description and Narrative

            bw6.write("Number:"+topic.get("Number"));
            bw6.write("\nContent:" + topic.get("Description")+topic.get("Narrative")+"\n");
            bw6.newLine();

            //7 using Title, Description and Narrative

            bw7.write("Number:"+topic.get("Number"));
            bw7.write("\nContent:" + topic.get("Title") + topic.get("Description")+topic.get("Narrative")+"\n");
            bw7.newLine();


        }

        br.close();
        bw1.close();
        bw2.close();
        bw3.close();
        bw4.close();
        bw5.close();
        bw6.close();
        bw7.close();



    }
}
