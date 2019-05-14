/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Josphine
 */
public class Cluster {
    private ArrayList<Document> member = new ArrayList<Document>();
    private Document center = new Document();
    private ArrayList<Document> listOfDocument = new ArrayList<Document>();

    public ArrayList<Document> getListOfDocument() {
        return listOfDocument;
    }

    public void setListOfDocument(ArrayList<Document> listOfDocument) {
        this.listOfDocument = listOfDocument;
    }
    
    public Cluster(){
        
    }

    public ArrayList<Document> getMember() {
        return member;
    }

    public void setMember(ArrayList<Document> member) {
        this.member = member;
    }

    public Document getCenter() {
        return center;
    }

    public void setCenter(Document center) {
        this.center = center;
    }
    
    public void preClustering(){
        //baca seluruh dokumen
        for(Document temp : listOfDocument){
            // baca l
        }
    }
    
}
