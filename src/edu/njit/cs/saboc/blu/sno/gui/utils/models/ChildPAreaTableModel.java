package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ChildPAreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Root Name", "Relationships", "Concepts"};
    private Object[][] data;
    private ArrayList<SCTPArea> pareas;

    public ChildPAreaTableModel(SCTPAreaTaxonomy pareaTaxonomy, ArrayList<SCTPArea> pareas) {
        setData(pareaTaxonomy, pareas);
    }

    public final void setData(SCTPAreaTaxonomy hierarchyData, ArrayList<SCTPArea> pareas) {
        this.pareas = pareas;

        data = new Object[pareas.size()][columnNames.length];

        for (int r = 0; r < pareas.size(); r++) {
            SCTPArea parea = pareas.get(r);

            String pareaRels = "";

            ArrayList<InheritedRelationship> relationships = new ArrayList<InheritedRelationship>(parea.getRelationships());

            for (int i = 0; i < relationships.size(); i++) {
                InheritedRelationship rel = relationships.get(i);
                String relStr = hierarchyData.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
                relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";
                pareaRels += relStr + (i < (relationships.size() - 1) ? "\n" : "");
            }

            data[r][0] = parea.getRoot().getName();
            data[r][1] = pareaRels;
            data[r][2] = new Integer(parea.getConceptCount());
        }

        this.fireTableDataChanged();
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

    public ArrayList<SCTPArea> getPAreas() {
        return pareas;
    }
}
