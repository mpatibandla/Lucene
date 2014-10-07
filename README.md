Lucene
======

Creating Lucene index for set of documents

Create index by using Lucene API, while using the index to solve a number of problems.
Each document is a collection of pre-defined fields, where a field supplies a field name and value. 
By using Lucene API (in Java), we can easily generate corpus index (inverted index, and, then, 
we can calculate TF and IDF by using Lucene search API.)
Now, we need to generate a Lucene index with the following fields: 
1.DOCNO, 
2. HEAD (merge two < HEAD >), 
3. <BYLINE> (merge two < HEAD >), 
4. <DATELINE>, and 
5. <TEXT>.
