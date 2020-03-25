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
		int hitspp = 10;
		int choice;
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
//		System.out.println("Parsing Query file.");
//		String path = "./Resource/cran";
//		//analyzer = new StandardAnalyzer();
//		QueryLoader ql = new QueryLoader(path, analyzer);
//		ql.loadQueries();
//		List<Query> queries = ql.getQueries();
//		try {
//			PrintWriter writer = new PrintWriter("./outputs.txt", "UTF-8");
//			for (Query query : queries) {
//				ScoreDoc[] hits = SearchFiles.doPagingSearch(query,hitspp,similarity);
//				for (int i = 0; i < hits.length; ++i) {
//					int docId = hits[i].doc;
//					double score = hits[i].score;
//					writer.println(queryCount + " 0 " + (docId + 1) + " " + (i + 1) + " " + score + " "+ choices);
//				}
//				queryCount++;   
//			}
//			writer.close();
//			System.out.println("Result is successfully written to output file");
//		} catch (Exception e) {
//			System.out.println("Error.\n");
//		}


	}


}
