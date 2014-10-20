import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
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
public class generateIndex{

	static String MergeTags(String s, String open, String close) throws IOException{
		String text="";
		String[] lines=s.split(System.getProperty("line.separator"));
		Pattern popen=Pattern.compile(open,Pattern.CASE_INSENSITIVE);
		Pattern pclose=Pattern.compile(close, Pattern.CASE_INSENSITIVE);
		Pattern pnew=Pattern.compile(open+"|"+close, Pattern.CASE_INSENSITIVE);
		int n=lines.length;
		int flag=0;
		for(int i=0;i<n;i++)
		{
			Matcher match = popen.matcher(lines[i]);
		if(match.find()){
		String []words = pnew.split(lines[i]);
		for(int j=0;j<words.length;j++){
			text=text+words[j];
			
		}
		flag=1;
		match=pclose.matcher(lines[i]);
		while(!match.find()){
			if(flag!=1){
				words=pnew.split(lines[i]);
				for(int j=0;j<words.length;j++){
					text=text+words[j];
				}
			}
			flag=0;
			i++;
			match=pclose.matcher(lines[i]);
		}
		}
		}
		return(open+text+close+" ");
	}
	
	
@SuppressWarnings({ })
public static void main(String args[]) throws IOException {

File docDir = new File("C:\\Users\\Manasa\\Documents\\Assignments and Work\\Fall 2014\\Info Retrieval\\corpus\\corpus\\");
File indexDir = new File("C:\\Users\\Manasa\\Desktop\\result\\");
Directory file = FSDirectory.open(docDir);
Directory dir = FSDirectory.open(indexDir);
Analyzer analyzer = new StandardAnalyzer();
IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
iwc.setOpenMode(OpenMode.CREATE);
IndexWriter	writer = new IndexWriter(dir, iwc);
for(File f : docDir.listFiles()){
	//taking the contents of file into string buffer, and then passing it to string builder
	StringBuilder stbu[] = new StringBuilder[1000];
/*	 StringBuilder sb = new StringBuilder();
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
}*/ //counting based on <DOC>
int l=-1;
String s[]= new String[1000];
BufferedReader br = new BufferedReader( new FileReader (f));
try {
String newline = br.readLine();
       while (newline != null) 
{ 
   if(newline.equals("<DOC>"))
   {       
	   l++;
	   s[l] = new String();
   }
        s[l] = s[l] + newline;
        s[l] = s[l] + "\n";
        newline = br.readLine();
} 
}
finally{
br.close();
}
//merging repetition of tags and appending it to the string builder
int count=l;
	for(int j=0;j<=count;j++)
	{
		stbu[j]= new StringBuilder();
		stbu[j]=stbu[j].append(MergeTags(s[j],"<DOCNO>","</DOCNO>"));
		stbu[j]=stbu[j].append(MergeTags(s[j],"<HEAD>","</HEAD>"));
		stbu[j]=stbu[j].append(MergeTags(s[j],"<BYLINE>","</BYLINE>"));
		stbu[j]=stbu[j].append(MergeTags(s[j],"<DATELINE>","</DATELINE>"));
		stbu[j]=stbu[j].append(MergeTags(s[j],"<TEXT>","</TEXT>"));
	} 
	//trecdoc parser to extract the tags, and adding them to the luceneDoc
for(int i=0;i<=count;i++){
String DOCNO = TrecDocParser.extract(stbu[i],"<DOCNO>","</DOCNO>", -1, null);
String TEXT = TrecDocParser.extract(stbu[i], "<TEXT>", "</TEXT>", -1, null);
String HEAD = TrecDocParser.extract(stbu[i], "<HEAD>", "</HEAD>", -1, null);
String BYLINE = TrecDocParser.extract(stbu[i], "<BYLINE>", "</BYLINE>", -1, null);
String DATELINE = TrecDocParser.extract(stbu[i], "<DATELINE>", "</DATELINE>", -1, null);
Document luceneDoc = new Document();
//adding to the luceneDoc
if(DOCNO!=null)
luceneDoc.add(new StringField("DOCNO",DOCNO,Field.Store.YES));
if(TEXT!=null)
luceneDoc.add(new TextField("TEXT", TEXT,Field.Store.YES));
if(HEAD!=null)
luceneDoc.add(new StringField("HEAD", HEAD,Field.Store.YES));
if(BYLINE!=null)
luceneDoc.add(new StringField("BYLINE", BYLINE,Field.Store.YES));
if(DATELINE!=null)
luceneDoc.add(new StringField("DATELINE", DATELINE,Field.Store.YES));
	writer.addDocument(luceneDoc);
}}
writer.forceMerge(1);
writer.commit();
writer.close();
//printing the required generated, token and vocabulary details
IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Manasa\\Desktop\\result\\")));
System.out.println("Total number of documents in the corpus:"+reader.maxDoc());
System.out.println("Number of documents containing the term \"new\" for field \"TEXT\": "+reader.docFreq(new Term("TEXT", "new")));
System.out.println("Number of occurences of \"new\" in the field \"TEXT\": "+reader.totalTermFreq(new Term("TEXT","new")));
Terms vocabulary = MultiFields.getTerms(reader, "TEXT");
System.out.println("Size of the vocabulary for this field:"+vocabulary.size());
System.out.println("Number of documents that have at least one term for this field: "+vocabulary.getDocCount());
System.out.println("Number of tokens for this field: "+vocabulary.getSumTotalTermFreq());
System.out.println("Number of postings for this field: "+vocabulary.getSumDocFreq());
TermsEnum	iterator = vocabulary.iterator(null);
System.out.println("\n*******Vocabulary-Start**********");
BytesRef byteRef;
	while(( byteRef = iterator.next()) != null) {
	String term = byteRef.utf8ToString();
	System.out.print(term+"\n");
	}
System.out.println("\n*******Vocabulary-End**********");
	reader.close(); 
}
}

