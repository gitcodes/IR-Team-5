
package IR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import IR.App.Similarity;

/** Simple command-line based search demo. */
public class SearchFiles {

  private SearchFiles() {}


public static ScoreDoc[] doPagingSearch(Query query,int hitsPerPage,String similarity) throws IOException {
  String index = "./index";

  IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
  IndexSearcher searcher = new IndexSearcher(reader);
  if (similarity == Similarity.BOOLEAN.toString())  searcher.setSimilarity(new BooleanSimilarity());
  if (similarity == Similarity.CLASSIC.toString())  searcher.setSimilarity(new ClassicSimilarity());
  if (similarity == Similarity.BM25.toString())  searcher.setSimilarity(new BM25Similarity());
 
TopDocs results = searcher.search(query, 5 * hitsPerPage);
ScoreDoc[] hits = results.scoreDocs;
reader.close();
return hits;

}

}