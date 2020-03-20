package IR.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.*;



public class CreateCranDocument{
private Integer corpusCount = 0;
// private List<CranDocument> cranDocsList ;
public List<Document> cranDocsList ;
private String path;
public CreateCranDocument(String path)
{
    this.cranDocsList = new ArrayList<>();
    this.path = path;
}
public void loadContentFromFile() {
    String docId = "";
    StringBuilder textAbstract = new StringBuilder();
    String authors = "";
    String bibliography = "";
    StringBuilder content = new StringBuilder();

    String tag = ".I";

    File file = new File(path);
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {

            // Returns a new tag if found else null
            String newTag = getTagIfAny(line);
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
                            cranDocsList.add(doc);
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
        Document doc = createDocument(docId, textAbstract.toString(), authors, bibliography, 
                content.toString());
                cranDocsList.add(doc);
        
    } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
    }
}

private Document createDocument(String docId, String title, String authors, String bibliography,
String content) throws IOException {

Document doc = new Document();
doc.add(new StringField("docId", docId, Field.Store.YES));
doc.add(new TextField("title", title, Field.Store.YES));
doc.add(new TextField("author", authors, Field.Store.YES));
doc.add(new TextField("bibliography", bibliography, Field.Store.YES));
doc.add(new TextField("content", content, Field.Store.YES));
return doc;
}
private  String getTagIfAny(String line) {
  String[] Cent = {".I",".T",".A",".B", ".W"};

    for (String tag : Cent) {
        if(line.contains(tag))
            return tag;
    }
    return null;
}

}
