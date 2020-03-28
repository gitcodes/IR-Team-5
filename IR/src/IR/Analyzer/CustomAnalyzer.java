package IR.Analyzer;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;

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

import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

public class CustomAnalyzer extends StopwordAnalyzerBase {
    public CustomAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

	@java.lang.Override
	protected TokenStreamComponents createComponents(String fieldName) {
		try {
		ResourceLoader resourceLoader = new ClasspathResourceLoader(ClassLoader.getSystemClassLoader());
		
		TokenizerModel tokenizerModel = OpenNLPOpsFactory.getTokenizerModel("en-token.bin", resourceLoader);
		
        NLPTokenizerOp tokenizerOp = new NLPTokenizerOp(tokenizerModel);
        SentenceModel sentenceModel = OpenNLPOpsFactory.getSentenceModel("en-sent.bin", resourceLoader);
        NLPSentenceDetectorOp sentenceDetectorOp = new NLPSentenceDetectorOp(sentenceModel);
        Tokenizer source = new OpenNLPTokenizer(
                AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, sentenceDetectorOp, tokenizerOp);
        
        POSModel posModel = OpenNLPOpsFactory.getPOSTaggerModel("en-pos-maxent.bin", resourceLoader);
        NLPPOSTaggerOp posTaggerOp = new NLPPOSTaggerOp(posModel);
        //Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(source);
        tokenStream = new LengthFilter(tokenStream, 4, 25);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, stopwords);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        TokenFilter filter = new OpenNLPPOSFilter(tokenStream, posTaggerOp);
        return new TokenStreamComponents(source, filter
        		);
		}
		catch(IOException e) {
			return null;
		}
		
		
	}

}

