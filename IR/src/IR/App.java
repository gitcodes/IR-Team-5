package IR;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.util.Scanner;
import IR.Helper.*;
import IR.Helper.DocumetParsers.*;
import IR.CustomAnalyzer;
import IR.Tagfilter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;

/**
 * Hello world!
 */
public final class App {
	private App() {
	}
	enum Analyzers {
		STANDARD,
		ENGLISH,
		CUSTOM;     
	}
	enum Similarity {
		CLASSIC,
		BM25,
		BOOLEAN,
		MULTI;     
	}
    private static CharArraySet getStopWordSet() throws IOException {
    	
    	FileInputStream stopfile =  new  FileInputStream("./Resource/stop");
        InputStreamReader inputStreamReader = new InputStreamReader(stopfile);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        CharArraySet stopWordSet = new CharArraySet(1000, true);
        while (bufferedReader.ready()) {
            stopWordSet.add(bufferedReader.readLine());
        }
        bufferedReader.close();
        return stopWordSet;
    }

	public static void main(String[] args) throws Exception {

		Analyzer analyzer;
		String similarity;
		String choices = "";
		boolean isIndexingSuccess ;
		int queryCount = 1;
		int hitspp = 200;
		int choice;
		String[] fields = {"text","headline"};
		System.out.println("Choose your Analyser\n 1.STANDARD \t 2.ENGLISH \t 3.CUSTOM");
		Scanner inp= new Scanner(System.in);
		choice = inp.nextInt();
		if (choice == 1 )
		{
			analyzer = new StandardAnalyzer();
			choices = choices + Analyzers.STANDARD.toString();
		}
		else if (choice == 2) 
		{
			analyzer = new EnglishAnalyzer(getStopWordSet());
			choices = choices + Analyzers.ENGLISH.toString();
		}
		else
		{
			analyzer = new CustomAnalyzer(getStopWordSet());
			choices = choices + Analyzers.CUSTOM.toString();
		}
		System.out.println("Choose your Similarity\n 1.BM25Similarity \t 2.MultiSimilarity");
		choice = inp.nextInt();
		if (choice == 1 )
		{
			similarity = Similarity.BM25.toString();
			choices = choices+ "-" + similarity;
		}
//		else if (choice == 1 )
//		{
//			similarity = Similarity.CLASSIC.toString();
//			choices = choices+ "-" + similarity;
//		}
//		else if (choice == 4 )
//		{
//			similarity = Similarity.MULTI.toString();
//			choices = choices+ "-" + similarity;
//		}
		else
		{
			similarity = Similarity.MULTI.toString();
			choices = choices+ "-" + similarity;
		}
		isIndexingSuccess =  IndexFiles.createIndex(analyzer);
		if (!isIndexingSuccess)
		{
			System.out.println("Indexing failed");
			return;
		}
		System.out.println("Indexing Successful.\n");
		System.out.println("Reading Queries from topic file.");
		Tagfilter tf = new Tagfilter();
		GenerateQueriesFromTopics generateQueriesFromTopics = new GenerateQueriesFromTopics();
		generateQueriesFromTopics.generateQueriesFromTopic();
		PrintWriter writer = new PrintWriter("./outputs.txt", "UTF-8");
		HashMap<String,Float> boosts = new HashMap<String,Float>();
		boosts.put("headline",3f);
		boosts.put("text",10f);
		boosts.put("others",1f);
        QueryParser parser = new MultiFieldQueryParser(fields, analyzer,boosts);
        Query query = null;
		for (QueryFieldsObject queryFieldsObject: generateQueriesFromTopics.getQueries()) {
			List<String> narrative = tf.narrativesplit(queryFieldsObject.narrative);
		    String narrativeSuggested = narrative.get(0);
		    String narrativeNotSuggested = narrative.get(1);
		    System.out.println("suggested---" +narrativeSuggested);
		    System.out.println("_______________________________________");
		    System.out.println("not suggested -----"+narrativeNotSuggested);
            String queryString = parser.escape(queryFieldsObject.title+" "+queryFieldsObject.description+" "+narrativeSuggested);
            if (narrativeNotSuggested.trim().length()>1) {
            	System.out.println(narrativeNotSuggested);
            	queryString = parser.escape(queryFieldsObject.title+" "+queryFieldsObject.description);
            	narrativeNotSuggested = parser.escape(narrativeNotSuggested);
            	Query queryNeg = new BoostQuery (parser.parse(narrativeNotSuggested),0.01f);
            	Query stringQuery = parser.parse(queryString);
            	BooleanQuery.Builder query1 = new BooleanQuery.Builder();
            	query1.add(stringQuery, Occur.SHOULD);
            	query1.add(queryNeg, Occur.SHOULD);
            	query = query1.build();
            }
            else {
            	query = parser.parse(queryString);
            }
            ScoreDoc[] hits = SearchFiles.doPagingSearch(query,hitspp,similarity,analyzer);
			for (int i = 0; i < hits.length; ++i) {
				String docNo = SearchFiles.getDocument(hits[i]);
				double score = hits[i].score;
				writer.println(queryFieldsObject.num + " 0 " +docNo + " " + (i + 1) + " " + score + " "+ choices);
			}
		}
		writer.close();
		SearchFiles.closeReader();
		System.out.println("Result is successfully written to output file");		

	}


}
