/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package IR;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;

import IR.Helper.*;

public class IndexFiles {
  
  //private Analyzer analyzer ;
  
  private IndexFiles() {
    
  }

  public static boolean createIndex(Analyzer analyzer) {
    boolean isSuccess = true;
    String indexPath = "./index";
    try {
      Directory dir = FSDirectory.open(Paths.get(indexPath));
      //Analyzer analyzer = new StandardAnalyzer();
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      iwc.setOpenMode(OpenMode.CREATE);
      IndexWriter writer = new IndexWriter(dir, iwc);
      String path = "./Resource/cran/cran.all.1400";
      CreateCranDocument cranDocuments = new CreateCranDocument(path);
      cranDocuments.loadContentFromFile();
      writer.addDocuments(cranDocuments.cranDocsList);
      writer.close();
      System.out.println("Index created in'" + indexPath + "'folder");
    } 
    catch (IOException e) {
      isSuccess = false;
    }
    return isSuccess;
  }

}















