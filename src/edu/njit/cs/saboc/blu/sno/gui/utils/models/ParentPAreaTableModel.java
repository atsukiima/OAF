package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ParentPAreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Root Name", "Parent Name", "Relationships", "Concepts"};
    private Object[][] data;
    private ArrayList<GroupParentInfo> parents;

    public ParentPAreaTableModel(PAreaTaxonomy pareaTaxonomy, ArrayList<GroupParentInfo> parents) {
        setData(pareaTaxonomy, parents);
    }

    public final void setData(PAreaTaxonomy pareaTaxonomy, ArrayList<GroupParentInfo> parents) {
        this.parents = parents;
        
        data = new Object[parents.size()][columnNames.length];

        for (int r = 0; r < parents.size(); r++) {
            PAreaSummary parea = pareaTaxonomy.getPAreaFromRootConceptId(parents.get(r).getParentPAreaRootId());

            String pareaRels = "";

            ArrayList<InheritedRelationship> relationships = parea.getRelationships();

            for (int i = 0; i < relationships.size(); i++) {
                InheritedRelationship rel = relationships.get(i);
                String relStr = pareaTaxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
                relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";
                pareaRels += relStr + (i < (relationships.size() - 1) ? "\n" : "");
            }

            data[r][0] = parea.getRoot().getName();
            data[r][1] = parents.get(r).getParentConcept().getName();
            data[r][2] = pareaRels;
            data[r][3] = new Integer(parea.getConceptCount());
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

    public ArrayList<GroupParentInfo> getParents() {
        return parents;
    }
}
