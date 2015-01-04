package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class AreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Relationships Defining Area", "Rels", "Regions", "PAreas", "Singletons"};
    private Object[][] data;

    public AreaTableModel(SCTPAreaTaxonomy pareaTaxonomy) {
        ArrayList<SCTArea> areas = pareaTaxonomy.getExplicitHierarchyAreas();

        data = new Object[areas.size()][5];

        for (int r = 0; r < areas.size(); r++) {

            String areaRels = "";

            for (InheritedRelationship rel : areas.get(r).getRelationships()) {   // Otherwise derive the title from its relationships.
                String relStr = pareaTaxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
                areaRels += (relStr + "\n");
            }

            data[r][0] = areaRels;
            data[r][1] = new Integer(areas.get(r).getRelationships().size());
            data[r][2] = new Integer(areas.get(r).getRegions().size());
            data[r][3] = new Integer(areas.get(r).getAllPAreas().size());

            int singletonCount = 0;

            for(SCTPArea p : areas.get(r).getAllPAreas()) {
                if(p.getConceptCount() == 1) {
                    singletonCount++;
                }
            }
            
            data[r][4] = new Integer(singletonCount);
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
