package IR.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;



/** Helper class to load all the queries from a file to the memory.
 * @author amit
 */
public class QueryLoader {


	private String filename;

	private List<Query> queries;

	private Integer queryCount = 0;

	
	private QueryParser qp;
	
	private MultiFieldQueryParser multiFieldQP;

	public QueryLoader(String filename, Analyzer analyzer) {
		
		this.qp = new QueryParser("content", analyzer);
		this.qp.setAllowLeadingWildcard(true);
		
		
		// Creating a multi field query parser
		HashMap<String,Float> boosts = new HashMap<String,Float>();
		boosts.put("title", 5f);
		boosts.put("author", 1f);
		boosts.put("content", 10f);
		this.multiFieldQP = new MultiFieldQueryParser(new String[] {"title","author","content"}, analyzer, boosts);
		//multiFieldQP.setAllowLeadingWildcard(true);
		
		this.filename = filename+"/cran.qry";
		
		this.queries = new ArrayList<>();
	}

	public List<Query> getQueries() {
		return queries;
	}

	public Integer getQueryCount() {
		return queryCount;
	}

	public void loadQueries() {
		String queryId = "";
		StringBuilder queryStr = new StringBuilder();

		String tag = ".I";

		File file = new File(filename);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				
				line = removeWildCardIfAny(line);
				
				// Returns a new tag if found else null
				String newTag = getTagIfAny(line);
				if (newTag != null) {
					tag = newTag;

					// progress to the next line where the actual content is
					if (!".I".equals(newTag))
						line = br.readLine();
				}

				if (".I".equals(tag)) {

					// Extract the docId
					queryId = line.split(" ")[1];

					queryCount++;
					// Add the previous document to the set of documents
					if (queryCount > 1) {
						Query query = createQuery(queryId, queryStr.toString());
						queries.add(query);
					}

					// reset the previous variables
					queryId = "";
					queryStr = new StringBuilder();

				} else if (".W".equals(tag)) {
//					if(line.contains("?")) {
//						line = line.replace('?', ' ');
//					}
					queryStr.append(line);
				}
			}

			// Save the last query
			Query query = createQuery(queryId, queryStr.toString());
			queries.add(query);

		} catch (IOException | ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Query createQuery(String queryStr) throws ParseException {
		Query q = this.multiFieldQP.parse(queryStr);
		return q;
	}

	private Query createQuery(String queryId, String queryStr) throws IOException, ParseException {
		Query q = null;
		
		q = multiFieldQP.parse(queryStr);
		// Query for each field and combine them into a boolean OR query
		// q = this.qp.parse(queryStr);
		return q;
	}

	/**
	 * Returns a tag or null if none present.
	 * 
	 * @param line
	 */
	private String getTagIfAny(String line) {
		String[] tags = {".I",".T",".A",".B", ".W"};

		for (String tag :tags) {
			if (line.contains(tag))
				return tag;
		}
		return null;
	}
	
	private String removeWildCardIfAny(String line) {
		if(line.contains("?")) 
			line = line.replace('?', ' ');
		
		if(line.contains("*")) 
			line = line.replace('*', ' ');
		
		return line;
	}
}
