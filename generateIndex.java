import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class generateIndex{
	
public static void main(String args[]){

File docDir = new File(args[0]);
File indexDir = new File(args[1]);
Directory file = FSDirectory.open(docDir);
Directory dir = FSDirectory.open(indexDir);
Analyzer analyzer = new StandardAnalyzer();
IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
iwc.setOpenMode(OpenMode.CREATE);
IndexWriter writer = new IndexWriter(dir, iwc);

//parsing the index file
for (File f : docDir.listFiles()) {
String fileName = f.getName();
String text = Files.readFromFile(f,"ASCII");
//Document d = new Document();
//d.add(new Field("file",fileName,Store.YES,Index.NOT_ANALYZED));
//d.add(new Field("text",text,Store.YES,Index.ANALYZED));
//indexWriter.addDocument(d);
}

for(File f : docDir.listFiles()){
Document luceneDoc = new Document();
luceneDoc.add(new StringField("DOCNO", DOCNO,Field.Store.YES));
luceneDoc.add(new TextField("TEXT", TEXT,Field.Store.YES));
writer.addDocument(luceneDoc);
}
int numDocs = writer.numDocs();
writer.forceMerge(1);
writer.commit();
writer.close();
}
}