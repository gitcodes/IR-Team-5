package IR;

import static com.kerinb.IR_proj2_group14.DocumentFiles.FinTimes.FinTimesLib.loadFinTimesDocs;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import IR.Helper.CreateFinancialTimesDocument;

public class IndexFinancialTimesFiles 
{
	
	 private IndexFinancialTimesFiles() 
	 {
		    
	  }

	  public static boolean createFinancialTimesIndex(Analyzer analyzer) 
	  {
	    boolean isSuccess = true;
	    String indexPath = "./index";
	    try {
	      Directory dir = FSDirectory.open(Paths.get(indexPath));
	      
	      //Analyzer analyzer = new StandardAnalyzer();
	      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	      iwc.setOpenMode(OpenMode.CREATE);
	      //IndexWriter writer = new IndexWriter(dir, iwc);
	      String path = "./Resource/ft";
	      List<String> financialTimesFiles = CreateFinancialTimesDocument.getFileNamesFromDir(path);
	      List<Document> financialTimesDocs = new ArrayList<>();
	      financialTimesDocs=loadFinancialTimesDocuments(financialTimesFiles);
			System.out.println("loaded financial times documents successfully");
	      //writer.addDocuments(finTimesFiles.financialTimesDocsList);
	      //writer.close();
	      System.out.println("Index created in'" + indexPath + "'folder");
	    } 
	    catch (IOException e) {
	      isSuccess = false;
	    }
	    return isSuccess;
	  }


}
