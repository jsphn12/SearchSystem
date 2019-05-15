/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvertedIndex {

    private ArrayList<Document> listOfDocument = new ArrayList<Document>();
    private ArrayList<Term> dictionary = new ArrayList<Term>();
    private ArrayList<Cluster> cluster = new ArrayList<Cluster>();
    public static final int NUMBER_OF_DOCUMENT_CLUSTER = 2;

   

    public InvertedIndex() {
    }

    public void addNewDocument(Document document) {
        listOfDocument.add(document);
    }

    public ArrayList<Document> getListOfDocument() {
        return listOfDocument;
    }

    public void setListOfDocument(ArrayList<Document> listOfDocument) {
        this.listOfDocument = listOfDocument;
    }

    public ArrayList<Term> getDictionary() {
        return dictionary;
    }

    public void setDictionary(ArrayList<Term> dictionary) {
        this.dictionary = dictionary;
    }

    public ArrayList<Posting> getUnsortedPostingList() {
        ArrayList<Posting> list = new ArrayList<Posting>();

        for (int i = 0; i < listOfDocument.size(); i++) {
            String[] termResult = listOfDocument.get(i).getListofTerm();
            for (int j = 0; j < termResult.length; j++) {
                Posting tempPosting = new Posting(termResult[j], listOfDocument.get(i));
                list.add(tempPosting);
            }
        }
        return list;
    }

    public ArrayList<Posting> getSortedPostingList() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        list = this.getUnsortedPostingList();
        Collections.sort(list);
        return list;
    }

    public ArrayList<Posting> getUnsortedPostingListWithTermNumber() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        for (int i = 0; i < getListOfDocument().size(); i++) {
            ArrayList<Posting> postingDocument = getListOfDocument().get(i).getListofPosting();
            for (int j = 0; j < postingDocument.size(); j++) {
                Posting tempPosting = postingDocument.get(j);
                list.add(tempPosting);
            }
        }
        return list;
    }

    public ArrayList<Posting> getSortedPostingListWithTermNumber() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        list = this.getUnsortedPostingListWithTermNumber();
        Collections.sort(list);
        return list;
    }

    public ArrayList<Posting> search(String kunci) {
        String[] q = kunci.split(" ");
        ArrayList<Posting> result = new ArrayList<>();
        for (int i = 0; i < q.length; i++) {
            String string = q[i];
            if (i == 0) {
                result = searchOneWord(q[i]);
            } else {
                ArrayList<Posting> result2 = searchOneWord(q[i]);
                result = intersection(result, result2);
            }
        }
        return result;
    }

    public ArrayList<Posting> intersection(ArrayList<Posting> p1, ArrayList<Posting> p2) {
        if (p1 == null || p2 == null) {
            return new ArrayList<>();
        }
        ArrayList<Posting> posting = new ArrayList<>();
        int index_p1 = 0;
        int index_p2 = 0;

        Posting post1 = p1.get(index_p1);
        Posting post2 = p2.get(index_p2);

        while (true) {
            if (post1.getDocument().getId() == post2.getDocument().getId()) {
                try {
                    posting.add(post1);
                    index_p1++;
                    index_p2++;
                    post1 = p1.get(index_p1);
                    post2 = p2.get(index_p2);
                } catch (Exception e) {
                    break;
                }
            } else if (post1.getDocument().getId() < post2.getDocument().getId()) {
                try {
                    index_p1++;
                    post1 = p1.get(index_p1);
                } catch (Exception e) {
                    break;
                }
            } else {
                try {
                    index_p2++;
                    post2 = p2.get(index_p2);
                } catch (Exception e) {
                    break;
                }
            }
        }
        return posting;
    }

    public ArrayList<Posting> searchOneWord(String kunci) {
        Term tempTerm = new Term(kunci);
        if (getDictionary().isEmpty()) {
            return null;
        } else {
            int positionTerm = Collections.binarySearch(dictionary, tempTerm);
            if (positionTerm < 0) {
                return null;
            } else {
                return dictionary.get(positionTerm).getPostingList();
            }
        }
    }

    public void makeDictionary() {
        ArrayList<Posting> list = this.getSortedPostingList();
        for (int i = 0; i < list.size(); i++) {
            if (getDictionary().isEmpty()) {
                Term term = new Term(list.get(i).getTerm());
                term.getPostingList().add(list.get(i));
                getDictionary().add(term);
            } else {
                Term tempTerm = new Term(list.get(i).getTerm());
                int position = Collections.binarySearch(getDictionary(), tempTerm);
                if (position < 0) {
                    tempTerm.getPostingList().add(list.get(i));
                    getDictionary().add(tempTerm);
                } else {
                    getDictionary().get(position).getPostingList().add(list.get(i));
                    Collections.sort(getDictionary().get(position).getPostingList());
                }
                Collections.sort(getDictionary());
            }
        }
    }

    public void makeDictionaryWithTermNumber() {
        ArrayList<Posting> list = this.getSortedPostingListWithTermNumber();
        for (int i = 0; i < list.size(); i++) {
            if (getDictionary().isEmpty()) {
                Term term = new Term(list.get(i).getTerm());
                term.getPostingList().add(list.get(i));
                getDictionary().add(term);
            } else {
                Term tempTerm = new Term(list.get(i).getTerm());
                int position = Collections.binarySearch(getDictionary(), tempTerm);
                if (position < 0) {
                    tempTerm.getPostingList().add(list.get(i));
                    getDictionary().add(tempTerm);
                } else {
                    getDictionary().get(position).getPostingList().add(list.get(i));
                    Collections.sort(getDictionary().get(position).getPostingList());
                }
                Collections.sort(getDictionary());
            }
        }
    }

    public int getDocFreq(String term) {
        Term tempTerm = new Term(term);
        int index = Collections.binarySearch(dictionary, tempTerm);
        if (index > 0) {
            ArrayList<Posting> tempPosting = dictionary.get(index).getPostingList();
            return tempPosting.size();
        } else {
            return -1;
        }
    }

    public double getInverseDocFreq(String term) {
        Term tempTerm = new Term(term);
        int index = Collections.binarySearch(dictionary, tempTerm);
        if (index > 0) {
            double N = listOfDocument.size();
            double ni = getDocFreq(term);
            double Nni = N / ni;
            return Math.log10(Nni);
        } else {
            return 0.0;
        }
    }

    public int getTermFreq(String term, int idDoc) {
        Document doc = new Document();
        doc.setId(idDoc);
        int index = Collections.binarySearch(listOfDocument, doc);
        if (index >= 0) {
            ArrayList<Posting> tempPost = getListOfDocument().get(index).getListofPosting();
            Posting post = new Posting();
            post.setTerm(term);
            int postIndex = Collections.binarySearch(tempPost, post);
            if (postIndex > 0) {
                return tempPost.get(postIndex).getNumberOfTerm();
            }
            return 0;
        }
        return 0;
    }

    public ArrayList<Posting> makeTFIDF(int idDocument) {
        ArrayList<Posting> result = new ArrayList<Posting>();
        Document temp = new Document(idDocument);
        int cari = Collections.binarySearch(listOfDocument, temp);
        if (cari >= 0) {
            temp = listOfDocument.get(cari);
            result = temp.getListofPosting();
            for (int i = 0; i < result.size(); i++) {
                String tempTerm = result.get(i).getTerm();
                double idf = getInverseDocFreq(tempTerm);
                int tf = result.get(i).getNumberOfTerm();
                double w = tf * idf;
                result.get(i).setWeight(w);
            }
            Collections.sort(result);
        } else {

        }
        return result;
    }

    public double getInnerProduct(ArrayList<Posting> p1, ArrayList<Posting> p2) {
        Collections.sort(p2);
        Collections.sort(p1);
        double result = 0.0;
        for (int i = 0; i < p1.size(); i++) {
            Posting temp = p1.get(i);
            boolean found = false;
            for (int j = 0; j < p2.size() && found == false; j++) {
                Posting temp1 = p2.get(j);
                if (temp1.getTerm().equalsIgnoreCase(temp.getTerm())) {
                    found = true;
                    result = result + temp1.getWeight() * temp.getWeight();
                }
            }
        }
        return result;
    }

    public ArrayList<Posting> getQueryPosting(String query) {
        Document temp = new Document(-1, query);
        ArrayList<Posting> result = temp.getListofPosting();
        for (int i = 0; i < result.size(); i++) {
            String tempTerm = result.get(i).getTerm();
            double idf = getInverseDocFreq(tempTerm);
            int tf = result.get(i).getNumberOfTerm();
            double w = tf * idf;
            result.get(i).setWeight(w);
        }
        Collections.sort(result);
        return result;
    }

    public double getLengthOfPosting(ArrayList<Posting> posting) {
        double result = 0;
        for (int i = 0; i < posting.size(); i++) {
            result += Math.pow(posting.get(i).getWeight(), 2);
        }
        return Math.sqrt(result);
    }

    public double getCosineSimilarity(ArrayList<Posting> posting, ArrayList<Posting> posting1) {
        double innerProduct = getInnerProduct(posting, posting1);
        double hasil = innerProduct / (Math.sqrt(getLengthOfPosting(posting) * getLengthOfPosting(posting1)));
        return hasil;
    }

    public ArrayList<SearchingResult> searchTFIDF(String query) {
        ArrayList<SearchingResult> result = new ArrayList<SearchingResult>();
        ArrayList<Posting> queryPostingList = getQueryPosting(query);
        for (int i = 0; i < listOfDocument.size(); i++) {
            Document doc = listOfDocument.get(i);
            int idDoc = doc.getId();
            ArrayList<Posting> tempDocWeight = makeTFIDF(idDoc);
            double hasilDotProduct = getInnerProduct(tempDocWeight, queryPostingList);
            if (hasilDotProduct > 0) {
                SearchingResult resultDoc = new SearchingResult(hasilDotProduct, doc);
                result.add(resultDoc);
            }
        }
        Collections.sort(result);
        return result;
    }

    public ArrayList<SearchingResult> searchCosineSimilarity(String query) {
        ArrayList<SearchingResult> result = new ArrayList<SearchingResult>();
        ArrayList<Posting> queryPostingList = getQueryPosting(query);
        for (int i = 0; i < listOfDocument.size(); i++) {
            Document doc = listOfDocument.get(i);
            int idDoc = doc.getId();
            ArrayList<Posting> tempDocWeight = makeTFIDF(idDoc);
            double cosineSimilarity = getCosineSimilarity(tempDocWeight, queryPostingList);
            if (cosineSimilarity > 0) {
                SearchingResult resultDoc = new SearchingResult(cosineSimilarity, doc);
                result.add(resultDoc);
            }
        }
        Collections.sort(result);
        return result;
    }

    public void readDirectory(File dir) {
        File[] listFile = dir.listFiles();
        int idDoc = listOfDocument.size() + 1;
        for (int i = 0; i < listFile.length; i++) {
            Document doc = new Document();
            try {
                doc.readFile(idDoc, listFile[i]);
            } catch (IOException ex) {
                Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
            addNewDocument(doc);
            idDoc++;
        }
        makeDictionaryWithTermNumber();
    }

    public void readOneFile(File dir) {
        int idDoc = listOfDocument.size() + 1;
        Document doc = new Document();
        try {
            doc.readFile(idDoc, dir);
            System.out.println(idDoc);
        } catch (IOException ex) {
            Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        addNewDocument(doc);
        makeDictionaryWithTermNumber();
        idDoc++;
    }

    public ArrayList<Cluster> getCluster() {
        return cluster;
    }

    public void setCluster(ArrayList<Cluster> cluster) {
        this.cluster = cluster;
    }

//    public void preClustering(){
//        // baca seluruh document
//        for(int i=0;i<listOfDocument.size();i++){
//            // baca idDoc
//            int idDoc = listOfDocument.get(i).getId();
//            // buat posting dengan nilai TF-IDFnya
//            listOfDocument.get(i).setListOfPosting(makeTFIDF(idDoc));
//            
//        }
//    }
    
}
