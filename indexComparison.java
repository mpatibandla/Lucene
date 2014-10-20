import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
//import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
//import org.apache.lucene.index.Terms;
//import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
//import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.lucene.benchmark.byTask.feeds.TrecDocParser;

@SuppressWarnings("unused")
public class indexComparison{

@SuppressWarnings({ "resource" })
public static void main(String args[]) throws IOException {

File docDir = new File("C:\\Users\\Manasa\\Documents\\Assignments and Work\\Fall 2014\\Info Retrieval\\corpus\\corpus\\");
File indexDir = new File("C:\\Users\\Manasa\\Desktop\\SA\\");//standard analyzer
File indexDir1 = new File("C:\\Users\\Manasa\\Desktop\\SimpleA\\");//simple analyzer result directory
File indexDir2 = new File("C:\\Users\\Manasa\\Desktop\\StopA\\");//stop analyzer result directory
File indexDir3 = new File("C:\\Users\\Manasa\\Desktop\\KA\\");//keyword analyzer result directory
Directory file = FSDirectory.open(docDir);
Directory dir = FSDirectory.open(indexDir);
Directory dir1 = FSDirectory.open(indexDir1);//simple analyzer
Directory dir2 = FSDirectory.open(indexDir2);//stop analyzer
Directory dir3 = FSDirectory.open(indexDir3);//keyword analyzer
Analyzer analyzer = new StandardAnalyzer();
Analyzer analyzer3 = new KeywordAnalyzer();
Analyzer analyzer2 = new StopAnalyzer();
Analyzer analyzer1 = new SimpleAnalyzer();
IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
IndexWriterConfig iwc1 = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer1);//simple analyzer
IndexWriterConfig iwc2 = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer2);//stop analyzer
IndexWriterConfig iwc3 = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer3);//keyword analyzer
iwc.setOpenMode(OpenMode.CREATE);
iwc1.setOpenMode(OpenMode.CREATE);
iwc2.setOpenMode(OpenMode.CREATE);
iwc3.setOpenMode(OpenMode.CREATE);
IndexWriter	writer = new IndexWriter(dir, iwc);
IndexWriter	writer1 = new IndexWriter(dir1, iwc1);//simple analyzer
IndexWriter	writer2 = new IndexWriter(dir2, iwc2);//stop analyzer
IndexWriter	writer3 = new IndexWriter(dir3, iwc3);//keyword analyzer
//parsing the index file
for(File f : docDir.listFiles()){
StringBuilder sb = new StringBuilder();
StringBuffer sbuffer = new StringBuffer();
String lineSep = System.getProperty("line.separator");
BufferedReader br = new BufferedReader( new FileReader (f));
String line = "";
while( ( line = br.readLine() ) != null ) {
    sbuffer.append(line);
    sbuffer.append(lineSep);}
sbuffer.toString();
sb.append(sbuffer);
Pattern p = Pattern.compile("<DOC>");
Matcher m = p.matcher(sb);
int count = 0;
while (m.find()){
	count +=1;
}

for(int i=0;i<count;i++){
String TEXT = TrecDocParser.extract(sb, "<TEXT>", "</TEXT>", -1, null);
Document luceneDoc = new Document();
if(TEXT!=null)
luceneDoc.add(new TextField("TEXT", TEXT,Field.Store.YES));
	writer.addDocument(luceneDoc);
	writer1.addDocument(luceneDoc);
	writer2.addDocument(luceneDoc);
	writer3.addDocument(luceneDoc);
}}
writer.forceMerge(1);
writer.commit();
writer.close();
writer1.forceMerge(1);
writer1.commit();
writer1.close();
writer2.forceMerge(1);
writer2.commit();
writer2.close();
writer3.forceMerge(1);
writer3.commit();
writer3.close();

IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Manasa\\Desktop\\SA\\")));
IndexReader reader3 = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Manasa\\Desktop\\KA\\")));
IndexReader reader1 = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Manasa\\Desktop\\SimpleA\\")));
IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Manasa\\Desktop\\StopA\\")));
System.out.println("Standard Analyzer:");
Terms vocabulary = MultiFields.getTerms(reader, "TEXT");
//Print the size of the vocabulary for <field>content</field>, only available per-segment.
	System.out.println("Size of the vocabulary for this field:"+vocabulary.size());
//Print the total number of documents that have at least one term for <field>TEXT</field>
	System.out.println("Number of documents that have at least one term for this field: "+vocabulary.getDocCount());

//Print the total number of tokens for <field>TEXT</field>
	System.out.println("Number of tokens for this field: "+vocabulary.getSumTotalTermFreq());
//Print the total number of postings for <field>TEXT</field>
	System.out.println("Number of postings for this field: "+vocabulary.getSumDocFreq());
//Print the vocabulary for <field>TEXT</field>
TermsEnum	iterator = vocabulary.iterator(null);
System.out.println("\n*******Vocabulary-Start**********");
BytesRef byteRef;
	while(( byteRef = iterator.next()) != null) {
	String term = byteRef.utf8ToString();
	System.out.print(term+"\n");
	}
System.out.println("\n*******Vocabulary-End**********");
	reader.close();
	System.out.println("Simple Analyzer:");
	Terms vocabulary1 = MultiFields.getTerms(reader1, "TEXT");
	//Print the size of the vocabulary for <field>content</field>, only available per-segment.
		System.out.println("Size of the vocabulary for this field:"+vocabulary1.size());
	//Print the total number of documents that have at least one term for <field>TEXT</field>
		System.out.println("Number of documents that have at least one term for this field: "+vocabulary1.getDocCount());

	//Print the total number of tokens for <field>TEXT</field>
		System.out.println("Number of tokens for this field: "+vocabulary1.getSumTotalTermFreq());
	//Print the total number of postings for <field>TEXT</field>
		System.out.println("Number of postings for this field: "+vocabulary1.getSumDocFreq());
	//Print the vocabulary for <field>TEXT</field>
	TermsEnum	iterator1 = vocabulary1.iterator(null);
	System.out.println("\n*******Vocabulary-Start**********");
	BytesRef byteRef1;
		while(( byteRef1 = iterator1.next()) != null) {
		String term = byteRef1.utf8ToString();
		System.out.print(term+"\n");
		}
	System.out.println("\n*******Vocabulary-End**********");
		reader1.close();
		System.out.println("Stop Analyzer:");
		Terms vocabulary2 = MultiFields.getTerms(reader2, "TEXT");
		//Print the size of the vocabulary for <field>content</field>, only available per-segment.
			System.out.println("Size of the vocabulary for this field:"+vocabulary2.size());
		//Print the total number of documents that have at least one term for <field>TEXT</field>
			System.out.println("Number of documents that have at least one term for this field: "+vocabulary2.getDocCount());

		//Print the total number of tokens for <field>TEXT</field>
			System.out.println("Number of tokens for this field: "+vocabulary2.getSumTotalTermFreq());
		//Print the total number of postings for <field>TEXT</field>
			System.out.println("Number of postings for this field: "+vocabulary2.getSumDocFreq());
		//Print the vocabulary for <field>TEXT</field>
		TermsEnum	iterator2 = vocabulary2.iterator(null);
		System.out.println("\n*******Vocabulary-Start**********");
		BytesRef byteRef2;
			while(( byteRef2 = iterator2.next()) != null) {
			String term = byteRef2.utf8ToString();
			System.out.print(term+"\n");
			}
		System.out.println("\n*******Vocabulary-End**********");
			reader2.close(); 
			System.out.println("Keyword Analyzer:");
			Terms vocabulary3 = MultiFields.getTerms(reader3, "TEXT");
			//Print the size of the vocabulary for <field>content</field>, only available per-segment.
				System.out.println("Size of the vocabulary for this field:"+vocabulary3.size());
			//Print the total number of documents that have at least one term for <field>TEXT</field>
				System.out.println("Number of documents that have at least one term for this field: "+vocabulary3.getDocCount());

			//Print the total number of tokens for <field>TEXT</field>
				System.out.println("Number of tokens for this field: "+vocabulary3.getSumTotalTermFreq());
			//Print the total number of postings for <field>TEXT</field>
				System.out.println("Number of postings for this field: "+vocabulary3.getSumDocFreq());
			//Print the vocabulary for <field>TEXT</field>
			TermsEnum	iterator3 = vocabulary3.iterator(null);
			System.out.println("\n*******Vocabulary-Start**********");
			BytesRef byteRef3;
				while(( byteRef3 = iterator3.next()) != null) {
				String term = byteRef3.utf8ToString();
				System.out.print(term+"\n");
				}
			System.out.println("\n*******Vocabulary-End**********");
				reader3.close(); 

}
}

