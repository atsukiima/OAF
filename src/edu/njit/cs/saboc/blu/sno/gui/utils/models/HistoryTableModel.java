package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Chris
 */
public class HistoryTableModel extends AbstractTableModel {

    private String[] columnNames = new String[]{"Root Name", "Concepts", "Parents", "Children"};
    
    private ArrayList<SCTPArea> data = new ArrayList<SCTPArea>();

    private SCTPAreaTaxonomy pareaTaxonomy;

    public HistoryTableModel(SCTPAreaTaxonomy pareaTaxonomy) {
        this.pareaTaxonomy = pareaTaxonomy;
    }

    public final void addEntry(SCTPArea parea) {
        data.add(0, parea);
        this.fireTableDataChanged();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        SCTPArea parea = data.get(row);

        switch(col) {
            case 0:
                return parea.getRoot().getName();
            case 1:
                return new Integer(parea.getConceptCount());
            case 2:
                return new Integer(parea.getParentIds().size());
            case 3:
                HashSet<Integer> children = pareaTaxonomy.getPAreaChildren(parea.getId());

                int value = children == null ? 0 : children.size();

                return new Integer(value);
        }

        return null;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public ArrayList<SCTPArea> getHistoryEntries() {
        return this.data;
    }
}
