/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import model.Document;
import model.InvertedIndex;
import model.SearchingResult;

/**
 *
 * @author admin
 */
public class testFileDokumen2 {

    public static void main(String[] args) throws IOException {
        InvertedIndex index = new InvertedIndex();
//        JFileChooser fc = new JFileChooser();
//        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//
//        int returnVal = fc.showOpenDialog(null);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File dir = fc.getSelectedFile();
//            index.readDirectory(dir);
//        }
        File dir = new File("D:\\SearchingSystemMachine\\File Dokumen");
        index.readDirectory(dir);
//        String tempString = "kehidupan";
//        int result2 = index.getDocFreq(tempString);
//        System.out.println("Number of Doc with " + tempString + " is " + result2);
        String query = "remaja";
        System.out.println("query = " + query);
        ArrayList<SearchingResult> hasilCari1 = index.searchCosineSimilarity(query);
        for (int i = 0; i < hasilCari1.size(); i++) {
            SearchingResult doc = hasilCari1.get(i);
            System.out.println("IdDokumen = " + doc.getDocument().getId() + " : " + doc.getDocument().getContent());
            System.out.println(doc.getSimilarity());
        }
//
//        // searching
//        System.out.println("");
//        System.out.println("query = " + query);
//        ArrayList<SearchingResult> hasilCari2 = index.searchCosineSimilarity(query);
//        for (int i = 0; i < hasilCari2.size(); i++) {
//            SearchingResult doc = hasilCari2.get(i);
//            System.out.println("IdDokumen = " + doc.getDocument().getId() + " : " + doc.getDocument().getContent());
//            System.out.println(doc.getSimilarity());
//        }

//        ArrayList<Document> listDoc = index.getListOfDocument();
//        for (int i = 0; i < listDoc.size(); i++) {
//            Document doc = listDoc.get(i);
//            System.out.println("Content : " + doc.getId());
//            System.out.println(doc.getContent());
//            System.out.println("");
//        }
    }
}
