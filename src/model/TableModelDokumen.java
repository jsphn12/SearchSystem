/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.SearchingResult;


public class TableModelDokumen extends AbstractTableModel {

    private List<SearchingResult> doc = new ArrayList<SearchingResult>();

    public TableModelDokumen(List<SearchingResult> doc) {
        this.doc = doc;
    }

    @Override
    public int getRowCount() {
        return doc.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SearchingResult b = doc.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return b.getDocument().getId();
            case 1:
                return b.getSimilarity();
            case 2:
                return b.getDocument().getContent();
            default:
                return "";
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id Doc";
            case 1:
                return "Ranking";
            case 2:
                return "Content";
            default:
                return "";
        }
    }
}
