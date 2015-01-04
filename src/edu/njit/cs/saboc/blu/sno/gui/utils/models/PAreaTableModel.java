package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.table.AbstractTableModel;

public class PAreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Root Name", "Concepts", "Parents", "Children"};
    private Object[][] data;

    public PAreaTableModel(SCTPAreaTaxonomy pareaTaxonomy, ArrayList<SCTPArea> pareas) {
        setData(pareaTaxonomy, pareas);
    }

    public final void setData(SCTPAreaTaxonomy pareaTaxonomy, ArrayList<SCTPArea> pareas) {
        data = new Object[pareas.size()][5];

        for (int r = 0; r < pareas.size(); r++) {
            SCTPArea parea = pareas.get(r);

            data[r][0] = parea.getRoot().getName();
            data[r][1] = new Integer(parea.getConceptCount());
            data[r][2] = new Integer(parea.getParentIds().size());

            HashSet<Integer> children = pareaTaxonomy.getPAreaChildren(parea.getId());

            int childCount = 0;

            if(children != null) {
                childCount = children.size();
            }
            
            data[r][3] = new Integer(childCount);
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
