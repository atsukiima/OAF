package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ParentPAreaTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Root Name", "Parent Name", "Relationships", "Concepts"};
    private Object[][] data;
    private ArrayList<GenericParentGroupInfo<Concept, SCTPArea>> parents;

    public ParentPAreaTableModel(SCTPAreaTaxonomy pareaTaxonomy, ArrayList<GenericParentGroupInfo<Concept, SCTPArea>> parents) {
        setData(pareaTaxonomy, parents);
    }

    public final void setData(SCTPAreaTaxonomy pareaTaxonomy, ArrayList<GenericParentGroupInfo<Concept, SCTPArea>> parents) {
        this.parents = parents;
        
        data = new Object[parents.size()][columnNames.length];

        for (int r = 0; r < parents.size(); r++) {
            SCTPArea parea = parents.get(r).getParentGroup();

            String pareaRels = "";

            ArrayList<InheritedRelationship> relationships = new ArrayList<InheritedRelationship>(parea.getRelationships());

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

    public ArrayList<GenericParentGroupInfo<Concept, SCTPArea>> getParents() {
        return parents;
    }
}
