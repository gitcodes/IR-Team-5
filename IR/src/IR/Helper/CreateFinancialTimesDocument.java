package IR.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class CreateFinancialTimesDocument 
{
	public static List<Document> financialTimesDocsList= new ArrayList<>();
	
	public static List<String> getFileNamesFromDir(String rootDirectory)
	{
		List<String> filesList = new ArrayList<>();
		try (Stream<Path> filePathStream=Files.walk(Paths.get(rootDirectory))) 
		{
		    filePathStream.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) 
		        {
		        	filesList.add(filePath.toString());
		            System.out.println(filePath);
		        }
		    });
		}
		catch (IOException e) 
		{
	        System.out.println(String.format("Exception inside directory: %s", rootDirectory));
	        System.out.println(String.format("Error Message: %s", e.getMessage()));
		}
		return filesList;
	
	}
	
	public static List<Document> loadFinancialTimesDocuments(List<String> financialTimesFiles) 
	{
	    String docId = "";
	    String authors = "";
	    String bibliography = "";
	    String tag = ".I";
	    StringBuilder textAbstract = new StringBuilder();
	    StringBuilder content = new StringBuilder();

	    for(String filePath : financialTimesFiles)
	    {
	    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
	    	{
		        String currentLine;
		        while ((currentLine = br.readLine()) != null) 
		        {

	            // Returns a new tag if found else null
	            String newTag = getTagIfAny(currentLine);
	            if (newTag != null) {
	                tag = newTag;

	                // progress to the next line where the actual content is
	                if(!".I".equals(newTag))
	                    line = br.readLine();
	            }

	            if (".I".equals(tag)) {

	                // Extract the docId
	                docId = line.split(" ")[1];

	                corpusCount++;
	                // Add the previous document to the set of documents
	                if (corpusCount > 1) {
	                    Document doc = createDocument(docId, textAbstract.toString(), authors, bibliography, 
	                            content.toString());
	                    financialTimesDocsList.add(doc);
	                }

	                // reset the previous variables
	                docId = "";
	                textAbstract = new StringBuilder();
	                authors = "";
	                bibliography = "";
	                content = new StringBuilder();

	            } else if (".T".equals(tag)) {
	                textAbstract.append(line);
	            } else if (".A".equals(tag)) {
	                authors += line;
	            } else if (".B".equals(tag)) {
	                bibliography += line;
	            } else if (".W".equals(tag)) {
	                content.append(line);
	            }
	        }
	    
	        // Save the last document
	        Document doc = createFinancialTimesDocument(docId, textAbstract.toString(), authors, bibliography, 
	                content.toString());
	        financialTimesDocsList.add(doc);
	        
	    } 
	    catch (IOException e) 
	    {
	        throw new RuntimeException(e.getMessage());
	    }
	 }
		return financialTimesDocsList;
	}

	private Document createFinancialTimesDocument(String docId, String title, String authors, String bibliography,
	String content) throws IOException {

	Document doc = new Document();
	doc.add(new StringField("docno", docId, Field.Store.YES));
	doc.add(new TextField("headline", title, Field.Store.YES));
	doc.add(new TextField("author", authors, Field.Store.YES));
	doc.add(new TextField("text", bibliography, Field.Store.YES));
	doc.add(new TextField("content", content, Field.Store.YES));
	
	
	
	Document document = new Document();

    document.add(new StringField("docno", finTimesObject.getDocNo(), Field.Store.YES));
    document.add(new TextField("headline", finTimesObject.getHeadline(), Field.Store.YES));
    document.add(new TextField("text", finTimesObject.getText(), Field.Store.YES));

    return document;
	return doc;
	}
	
	//Function to get tags
	private  String getTagIfAny(String line) 
	{
	  String[] tags = {"<DOC>","</DOC>","<DOCNO>","</DOCNO>","<DOCID>","</DOCID>","<TEXT>",
			  "</TEXT>","<HEADLINE>","</HEADLINE>", "<BYLINE>","</BYLINE>","<CORRECTION>",
			  "</CORRECTION>","<CORRECTION-DATE>","</CORRECTION_DATE>",
			  };

	    for (String tag : tags) 
	    {
	        if(line.contains(tag))
	            return tag;
	    }
	    return null;
	}

	
}
