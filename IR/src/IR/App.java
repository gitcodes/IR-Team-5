package IR;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.util.Scanner;
import IR.Helper.*;
import IR.Helper.DocumetParsers.*;

import org.apache.lucene.analysis.Analyzer;
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
		BOOLEAN;     
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
		System.out.println("Choose your Analyser\n 1.STANDARD \t 2.ENGLISH");
		Scanner inp= new Scanner(System.in);
		choice = inp.nextInt();
		if (choice == 1 )
		{
			analyzer = new StandardAnalyzer();
			choices = choices + Analyzers.STANDARD.toString();
		}
		else
		{
			analyzer = new EnglishAnalyzer();
			choices = choices + Analyzers.ENGLISH.toString();
		}
		System.out.println("Choose your Similarity\n 1.ClassicSimilarity \t 2.BM25Similarity \t 3.BooleanSimilarity");
		choice = inp.nextInt();
		if (choice == 2 )
		{
			similarity = Similarity.BM25.toString();
			choices = choices+ "-" + similarity;
		}
		else if (choice == 1 )
		{
			similarity = Similarity.CLASSIC.toString();
			choices = choices+ "-" + similarity;
		}
		else
		{
			similarity = Similarity.BOOLEAN.toString();
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
		
		GenerateQueriesFromTopics generateQueriesFromTopics = new GenerateQueriesFromTopics();
		generateQueriesFromTopics.generateQueriesFromTopic();
		PrintWriter writer = new PrintWriter("./outputs.txt", "UTF-8");
		HashMap<String,Float> boosts = new HashMap<String,Float>();
		boosts.put("headline", 5f);
		boosts.put("text", 10f);
        QueryParser parser = new MultiFieldQueryParser(fields, analyzer,boosts);
		for (QueryFieldsObject queryFieldsObject: generateQueriesFromTopics.getQueries()) {
            String queryString = parser.escape(queryFieldsObject.title+" "+queryFieldsObject.description+" "+queryFieldsObject.narrative);
            Query query = parser.parse(queryString);
            ScoreDoc[] hits = SearchFiles.doPagingSearch(query,hitspp,similarity);
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
