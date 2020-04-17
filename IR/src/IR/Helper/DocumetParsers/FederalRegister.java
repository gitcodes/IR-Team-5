package IR.Helper.DocumetParsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import IR.Helper.Utility;

public class FederalRegister {
	public List<org.apache.lucene.document.Document> frDocList;
	static final String frPath =  "./Resource/fr94";

	public FederalRegister() {
		this.frDocList = new ArrayList<>();
	} 

	public void loadContentFromFile() 
	{
		String docNo;
		String  parent;
		String  text;
		File file;
		try {

			List<String> pathList = Utility.getPathForAllFiles(frPath);

			for (String path : pathList)
			{
				file = new File(path); 
				Document doc = Jsoup.parse(file,"UTF-8");

				Elements elements = doc.getElementsByTag(EnumTagContainer.FederalRegisterTags.DOC.toString());

				for (Element element : elements) {

					docNo = element.select(EnumTagContainer.FederalRegisterTags.DOCNO.toString()).text();
					parent = element.select(EnumTagContainer.FederalRegisterTags.PARENT.toString()).text();
					text = element.select(EnumTagContainer.FederalRegisterTags.TEXT.toString()).text();			 
					frDocList.add(createDocument(docNo,parent,text));
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static org.apache.lucene.document.Document createDocument(String docNo,String  parent,String  text) throws IOException{
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();

		document.add(new StringField("docNo", docNo, Field.Store.YES));
		document.add(new TextField("parent", parent, Field.Store.YES));
		document.add(new TextField("text",text, Field.Store.YES));
		document.add(new TextField("others", docNo + parent,Field.Store.YES));
		return document;
	}	

}

