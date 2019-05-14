/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class Document implements Comparable<Document> {
    
    private int id;
    private String content;
    private String realContent;
    private String title;
    
    private ArrayList<Posting> listOfPosting = new ArrayList<Posting>();
    // list posting untuk keperluan clustering document
    


    public Document() {
    }

    public Document(int id) {
        this.id = id;
    }

    public Document(String content) {
        this.content = content;
        this.realContent = content;
    }

    public Document(int id, String content) {
        this.id = id;
        this.content = content;
        this.realContent = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getListofTerm() {
        String value = this.getContent();
        value = value.replaceAll("[.,?!]", "");
        value = value.replaceAll("\n", " ");
        return value.split(" ");
    }

    @Override
    public int compareTo(Document doc) {
        return id - doc.getId();
    }

    @Override
    public String toString() {
        return "Document{" + "id=" + id + ", content=" + content + ", realContent=" + realContent + '}';
    }

    public ArrayList<Posting> getListofPosting() {
        String tempString[] = getListofTerm();
        ArrayList<Posting> result = new ArrayList<Posting>();
        for (int i = 0; i < tempString.length; i++) {
            if (i == 0) {
                Posting temPosting = new Posting(tempString[0], this);
                result.add(temPosting);
            } else {
                Collections.sort(result);
                Posting temPosting = new Posting(tempString[i], this);
                int indexCari = Collections.binarySearch(result, temPosting);
                if (indexCari < 0) {
                    result.add(temPosting);
                } else {
                    int tempNumber = result.get(indexCari).getNumberOfTerm() + 1;
                    result.get(indexCari).setNumberOfTerm(tempNumber);
                }
            }
        }
        return result;
    }

    public void readFile(int idDoc, File file) throws FileNotFoundException, IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        String name = file.getName().replace(".txt", "");
        setTitle(name);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        this.id = idDoc;
        this.content = buffer.toString();
        this.realContent = buffer.toString();
    }

    public void removeStopWords() {
        String text = content;
        Version matchVersion = Version.LUCENE_7_7_0;
        Analyzer analyzer = new StandardAnalyzer();
        analyzer.setVersion(matchVersion);

        CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
        TokenStream tokenStream = analyzer.tokenStream("myField", new StringReader(text));

        tokenStream = new org.apache.lucene.analysis.StopFilter(tokenStream, stopWords);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (IOException ex) {
            System.out.println("Exception : " + ex);
        }

        content = sb.toString();
    }

    public void EnglishStemming() {
        String text = content;
        Version matchVersion = Version.LUCENE_7_7_0;
        Analyzer analyzer = new StandardAnalyzer();
        analyzer.setVersion(matchVersion);

        TokenStream tokenStream = analyzer.tokenStream("myField", new StringReader(text));

        tokenStream = new PorterStemFilter(tokenStream);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (IOException ex) {
            System.out.println("Exception : " + ex);
        }
        content = sb.toString();
    }

    public void IndonesianStemming() {
        String text = content;
        Version matchVersion = Version.LUCENE_7_7_0;
        Analyzer analyzer = new IndonesianAnalyzer();
        analyzer.setVersion(matchVersion);

        TokenStream tokenStream = analyzer.tokenStream("myField", new StringReader(text));

        tokenStream = new IndonesianStemFilter(tokenStream);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (IOException ex) {
            System.out.println("Exception : " + ex);
        }
        content = sb.toString();
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }

    public ArrayList<Posting> getListOfPosting() {
        return listOfPosting;
    }

    public void setListOfPosting(ArrayList<Posting> listOfPosting) {
        this.listOfPosting = listOfPosting;
    }
    
    
}
