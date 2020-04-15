package IR;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;

import java.io.IOException;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.opennlp.OpenNLPLemmatizerFilter;
import org.apache.lucene.analysis.opennlp.OpenNLPPOSFilter;
import org.apache.lucene.analysis.opennlp.OpenNLPTokenizer;
import org.apache.lucene.analysis.opennlp.tools.NLPPOSTaggerOp;
import org.apache.lucene.analysis.opennlp.tools.NLPSentenceDetectorOp;
import org.apache.lucene.analysis.opennlp.tools.NLPTokenizerOp;
import org.apache.lucene.analysis.opennlp.tools.OpenNLPOpsFactory;
import org.tartarus.snowball.ext.EnglishStemmer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.lucene.analysis.snowball.SnowballFilter;

public class CustomAnalyzer extends StopwordAnalyzerBase {
    public CustomAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

   @Override
   protected TokenStreamComponents createComponents(String fieldName) {
     final Tokenizer source = new StandardTokenizer();
     TokenStream result = new EnglishPossessiveFilter(source);
     result = new LowerCaseFilter(result);
     result = new StopFilter(result, stopwords);
     //result = new LengthFilter(result, 4, 200);
     result = new SnowballFilter(result, new EnglishStemmer());
     //result = new PorterStemFilter(result);
     result = new TrimFilter(result);
     return new TokenStreamComponents(source, result);
   }

   @Override
   protected TokenStream normalize(String fieldName, TokenStream in) {
     return new LowerCaseFilter(in);
   }
		
		
		
	

}

