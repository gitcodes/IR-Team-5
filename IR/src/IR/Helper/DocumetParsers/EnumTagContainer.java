package IR.Helper.DocumetParsers;

public class EnumTagContainer {
	
	 static enum FinancialTimesTags {
		 DOC,
		 DOCNO,
		 PROFILE,
		 DATE,
		 HEADLINE,
		 TEXT,
		 PUB,
		 PAGE;
	 }

    static enum FederalRegisterTags {
    	DOC,
    	DOCNO,
    	PARENT,
    	TEXT,
    	USDEPT,        
    	AGENCY, 
    	USBUREAU,       
    	DOCTITLE,
    	ADDRESS, 
    	FURTHER, 
    	SUMMARY,       
    	ACTION,        
    	SIGNER,  
    	SIGNJOB, 
    	SUPPLEM,      
    	BILLING, 
    	FRFILING,
    	DATE,    
    	CFRNO,   
    	RINDOCK; 

    }
    
    static enum ForeignBroadcastISTags {
    	DOC,
    	DOCNO,
    	HT,
    	HEADER,
    	AU,        
    	DATE1,
    	TEXT; 

    }
    static enum LosAngelTimesTags {
    	DOC,
    	DOCNO,
    	P,
    	HEADLINE,
    	SUBJECT,
    	TEXT,
    	BYLINE,
    	SECTION,
    	LENGTH,
    	DATE,
    	TYPE,
    	GRAPHIC
    	
    }

}
