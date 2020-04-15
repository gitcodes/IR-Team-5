
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
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
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
  private static IndexSearcher searcher;
  static IndexReader reader ;
  private SearchFiles() {}


public static ScoreDoc[] doPagingSearch(Query query,int hitsPerPage,String similarity,Analyzer analyzer) throws IOException {
  String index = "./index";

  reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
  searcher = new IndexSearcher(reader);
  if (similarity == Similarity.BOOLEAN.toString())  searcher.setSimilarity(new BooleanSimilarity());
  if (similarity == Similarity.CLASSIC.toString())  searcher.setSimilarity(new ClassicSimilarity());
  if (similarity == Similarity.BM25.toString())  searcher.setSimilarity(new BM25Similarity());
  try {
	  	ScoreDoc[] hits = {};
		Query queryexpnded= expandQuery(searcher,query,hits,reader,analyzer);
	
		TopDocs results = searcher.search(queryexpnded, 5 * hitsPerPage);
		hits = results.scoreDocs;

return hits;
  } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}

}

public static String getDocument( ScoreDoc hit) throws IOException {
	Document hitDoc = searcher.doc(hit.doc);
	String docNum = hitDoc.get("docNo");
	return docNum;
}
private static Query expandQuery(IndexSearcher searcher, Query query, ScoreDoc[] hits,
		IndexReader reader,Analyzer analyzer) throws Exception {
	BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
	TopDocs topDocs = searcher.search(query, 10);

	for (ScoreDoc score : topDocs.scoreDocs) {
		Document hitDoc = reader.document(score.doc);
		String fieldText = hitDoc.getField("text").stringValue();
		String[] moreLikeThisField = { "text" };
		MoreLikeThisQuery expandedQueryMoreLikeThis = new MoreLikeThisQuery(fieldText, moreLikeThisField, analyzer,
				"text");
		Query expandedQuery = expandedQueryMoreLikeThis.rewrite(reader);
		queryBuilder.add(expandedQuery, Occur.SHOULD);
	}
	return queryBuilder.build();
}

public static void closeReader() throws IOException {
	reader.close();
}

}