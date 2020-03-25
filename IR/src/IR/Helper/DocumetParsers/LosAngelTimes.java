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

public class LosAngelTimes {
	public List<org.apache.lucene.document.Document> latimesDocList;
	static final String latimesPath =  "./Resource/latimes";

	public LosAngelTimes() {
		this.latimesDocList = new ArrayList<>();
	} 

	public void loadContentFromFile() 
	{
		String  docNo;
		String  text;
		String  byline;
		String  headline;
		String  date;
		File file;
		try {

			List<String> pathList = Utility.getPathForAllFiles(latimesPath);

			for (String path : pathList)
			{
				file = new File(path); 
				Document doc = Jsoup.parse(file,"UTF-8");
				Elements elements = doc.getElementsByTag(EnumTagContainer.LosAngelTimesTags.DOC.toString());
				for (Element element : elements) {

					docNo = element.select(EnumTagContainer.LosAngelTimesTags.DOCNO.toString()).text();
					text = element.select(EnumTagContainer.LosAngelTimesTags.TEXT.toString()).text();
					byline = element.select(EnumTagContainer.LosAngelTimesTags.BYLINE.toString()).text();
					headline = element.select(EnumTagContainer.LosAngelTimesTags.HEADLINE.toString()).text();	
					date = element.select(EnumTagContainer.LosAngelTimesTags.DATE.toString()).text();
					latimesDocList.add(createDocument(docNo,text,byline, headline,date));
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static org.apache.lucene.document.Document createDocument(String docNo,String text,String  byline,String  headline, String date) throws IOException{
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();

		document.add(new StringField("docNo", docNo, Field.Store.YES));
		document.add(new TextField("Text", text, Field.Store.YES));
		document.add(new TextField("Author", byline, Field.Store.YES));
		document.add(new TextField("Headline",headline, Field.Store.YES));
		document.add(new TextField("Date",date, Field.Store.YES));
		return document;
	}	
	

}
