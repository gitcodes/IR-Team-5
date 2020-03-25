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

public class ForeignBroadcastIS {
	
	public List<org.apache.lucene.document.Document> fbisDocList;
	static final String fbisPath =  "./Resource/fbis";

	public ForeignBroadcastIS() {
		this.fbisDocList = new ArrayList<>();
	} 

	public void loadContentFromFile() 
	{
		String docNo;
		String  ht;
		String  header;
		String  text;
		File file;
		try {

			List<String> pathList = Utility.getPathForAllFiles(fbisPath);

			for (String path : pathList)
			{
				file = new File(path); 
				Document doc = Jsoup.parse(file,"UTF-8");
				Elements elements = doc.getElementsByTag(EnumTagContainer.ForeignBroadcastISTags.DOC.toString());
				for (Element element : elements) {

					docNo = element.select(EnumTagContainer.ForeignBroadcastISTags.DOCNO.toString()).text();
					ht = element.select(EnumTagContainer.ForeignBroadcastISTags.HT.toString()).text();
					header = element.select(EnumTagContainer.ForeignBroadcastISTags.HEADER.toString()).text();
					text = element.select(EnumTagContainer.ForeignBroadcastISTags.TEXT.toString()).text();			 
					fbisDocList.add(createDocument(docNo,ht,header,text));
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static org.apache.lucene.document.Document createDocument(String docNo,String  ht,String  header,String  text) throws IOException{
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();

		document.add(new StringField("docNo", docNo, Field.Store.YES));
		document.add(new TextField("ht", ht, Field.Store.YES));
		document.add(new TextField("header", header, Field.Store.YES));
		document.add(new TextField("text",text, Field.Store.YES));
		return document;
	}	


}
