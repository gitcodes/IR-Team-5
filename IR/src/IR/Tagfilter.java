package IR;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class Tagfilter {

    POSTaggerME tagger = null;
    POSModel model = null;

    public void initialize(String lexiconFileName) {
        try {
            InputStream modelStream =  getClass().getResourceAsStream(lexiconFileName);
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String tag(String text){
        initialize("/en-pos-maxent.bin");
        String str =""; 
        try {
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                if (tagger != null) {
                    String[] sentences = detectSentences(text);
                    for (String sentence : sentences) {
                        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                                .tokenize(sentence);
                        String[] tags = tagger.tag(whitespaceTokenizerLine);
                        for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                            String word = whitespaceTokenizerLine[i].trim();
                            String tag = tags[i].trim();
                            //System.out.print(word+ ":"+ tag +" ");
                            if(tag.equals("NN")|| tag.equals("NNS"))
                            {
                            str+=(word + " ");
                            }
                        }
                    }
                }
            }
        return str.replaceAll("[-+.^:,]","");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String[] detectSentences(String paragraph) throws IOException {

        InputStream modelIn = getClass().getResourceAsStream("/en-sent.bin");
        final SentenceModel sentenceModel = new SentenceModel(modelIn);
        modelIn.close();

        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        String sentences[] = sentenceDetector.sentDetect(paragraph);
//        for (String sent : sentences) {
//            System.out.println(sent);
//        }
        return sentences;
    }
    public static void main(String[] args) throws Exception {
    	Tagfilter tf = new Tagfilter();
    	if("sd" == "sd")
    		System.out.println("yes");
    	tf.tag("The date of the storm, the area affected, and the extent of \r\n" + 
    			"damage/casualties are all of interest.  Documents that describe\r\n" + 
    			"the damage caused by a tropical storm as \"slight\", \"limited\", or\r\n" + 
    			"\"small\" are not relevant. ");
    }
    static List<String> narrativesplit(String text) {

		/*
		 * First splits based on dot and removes sentences that include
		 * "not relevant" And removes phrases like "a relevant document",
		 * "a document will","to be relevant", "relevant documents" and
		 * "a document must"
		 */
		StringBuilder posResult = new StringBuilder();
		StringBuilder negResult = new StringBuilder();
		String[] narativeSplit = text.toLowerCase().split("[/./;/,]");
		List<String> result = new ArrayList<String>();
		for (String sec : narativeSplit) {

			if (!sec.contains("not relevant") && !sec.contains("irrelevant")) {

				String re = sec.replaceAll(
						"a relevant document|a document will|to be relevant|relevant documents|a document must|relevant|will contain|will discuss|will provide|must cite",
						" ");
				posResult.append(re+" ");
			
			} else {
				String re = sec.replaceAll("are also not relevant|are not relevant|are irrelevant|is not relevant", " ");
				negResult.append(re+" ");
		
			}
		}
		result.add(posResult.toString());
		result.add(negResult.toString());

		return result;
	}

    public static String filter(String str) {
        return str.replaceAll("documents", " ")
          .replaceAll("document", " ")
          .replaceAll("relevant", " ")
          .replaceAll("include", " ")
          .replaceAll("discussions", " ")
          .replaceAll("discussion", " ")
          .replaceAll("discuss", " ")
          .replaceAll("results", " ")
          .replaceAll("result", " ")
          .replaceAll("describing", " ")
          .replaceAll("described", " ")
          .replaceAll("describe", " ")
          .replaceAll("provides", " ")
          .replaceAll("provide", " ")
          //.replaceAll("mention", " ")
          .replaceAll("find", " ")
          .replaceAll("information", " ");
      }
    static String minus(String Uline, String Iline) {
        Set<String> result = new HashSet<String>(Arrays.asList(Uline.split(" ")));
        Set<String> IlineSet = new HashSet<String>(Arrays.asList(Iline.split(" ")));
        result.retainAll(IlineSet);
        for (String str : result){
            Uline = Uline.replaceAll(str, "");
        }
        return Uline;
    }
}


