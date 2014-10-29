package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class AreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Relationships Defining Area", "Rels", "Regions", "PAreas", "Singletons"};
    private Object[][] data;

    public AreaTableModel(PAreaTaxonomy pareaTaxonomy) {
        ArrayList<Area> areas = pareaTaxonomy.getExplicitHierarchyAreas();

        data = new Object[areas.size()][5];

        for (int r = 0; r < areas.size(); r++) {

            String areaRels = "";

            for (long relId : areas.get(r).getRelationships()) {   // Otherwise derive the title from its relationships.
                String relStr = pareaTaxonomy.getLateralRelsInHierarchy().get(relId);
                areaRels += (relStr + "\n");
            }

            data[r][0] = areaRels;
            data[r][1] = new Integer(areas.get(r).getRelationships().size());
            data[r][2] = new Integer(areas.get(r).getRegions().size());
            data[r][3] = new Integer(areas.get(r).getAllPAreas().size());

            int singletonCount = 0;

            for(PAreaSummary p : areas.get(r).getAllPAreas()) {
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
