package IR.Helper.DocumetParsers;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import IR.Helper.Utility;

import java.io.File;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import java.io.IOException;
import org.apache.lucene.document.Field;



public class FinancialTimes {

	public List<org.apache.lucene.document.Document> ftDocList;
	static final String ftPath =  "./Resource/ft";

	public FinancialTimes() {
		this.ftDocList = new ArrayList<>();
	} 

	public void loadContentFromFile() 
	{
		String headline;
		String  docno;
		String  text;
		File file;
		try {
			
			List<String> pathList = Utility.getPathForAllFiles(ftPath);

			for (String path : pathList)
			{
				file = new File(path); 
				Document doc = Jsoup.parse(file,"UTF-8");

				Elements elements = doc.getElementsByTag(EnumTagContainer.FinancialTimesTags.DOC.toString());

				for (Element element : elements) {

					headline = element.select(EnumTagContainer.FinancialTimesTags.HEADLINE.toString()).text();
					docno = element.select(EnumTagContainer.FinancialTimesTags.DOCNO.toString()).text();
					text = element.select(EnumTagContainer.FinancialTimesTags.TEXT.toString()).text();			 
					ftDocList.add(createDocument(headline,docno,text));
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static org.apache.lucene.document.Document createDocument(String headline,String  docno,String  text) throws IOException{
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();

		document.add(new StringField("docNo", docno, Field.Store.YES));
		document.add(new TextField("headline", headline, Field.Store.YES));
		document.add(new TextField("text",text, Field.Store.YES));
		document.add(new TextField("others", docno,Field.Store.YES));
		return document;
	}	

}
