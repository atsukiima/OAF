package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Chris
 */
public class PAreaLevelTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Level", "Areas", "PAreas", "Concepts", "Rels"};
    private Object[][] data;

    private class LevelData {
        public int areaCount = 0;
        public int relCount = 0;
        public int pareaCount = 0;
        public int conceptCount = 0;
    }

    public PAreaLevelTableModel(SCTPAreaTaxonomy pareaTaxonomy) {
        
        ArrayList<SCTArea> areas = (ArrayList<SCTArea>)pareaTaxonomy.getExplicitHierarchyAreas().clone();
        
        Collections.sort(areas, new Comparator<SCTArea>() {
            public int compare(SCTArea a, SCTArea b) {
                Integer aCount = a.getRelationships().size();
                Integer bCount = b.getRelationships().size();
                
                return aCount.compareTo(bCount);
            }
        });

        ArrayList<LevelData> levelData = new ArrayList<LevelData>();

        SCTArea lastArea = areas.get(0);
        LevelData level = new LevelData();
        ArrayList<SCTPArea> levelPAreas = new ArrayList<SCTPArea>();

        for(SCTArea a : areas) {
            if(lastArea.getRelationships().size() != a.getRelationships().size()) {
                level.relCount = lastArea.getRelationships().size();
                
                level.conceptCount = pareaTaxonomy.getDataSource().getConceptCountInPAreaHierarchy(
                        pareaTaxonomy, levelPAreas);

                levelData.add(level);
                level = new LevelData();
                levelPAreas.clear();
            }

            level.areaCount += 1;
            level.pareaCount += a.getAllPAreas().size();

            levelPAreas.addAll(a.getAllPAreas());

            lastArea = a;
        }

        level.relCount = lastArea.getRelationships().size();
        level.conceptCount = pareaTaxonomy.getDataSource().getConceptCountInPAreaHierarchy(
                        pareaTaxonomy, levelPAreas);

        levelData.add(level);

        data = new Object[levelData.size() + 1][5];

        for (int r = 0; r < levelData.size(); r++) {

            data[r][0] = new Integer(r);
            data[r][1] = new Integer(levelData.get(r).areaCount);
            data[r][2] = new Integer(levelData.get(r).pareaCount);
            data[r][3] = new Integer(levelData.get(r).conceptCount);
            data[r][4] = new Integer(levelData.get(r).relCount);
        }

        int totalAreaCount = 0;
        int totalPAreaCount = 0;
        int totalConceptCount = 0;

        for(LevelData lvlData : levelData) {
            totalAreaCount += lvlData.areaCount;
            totalPAreaCount += lvlData.pareaCount;
            totalConceptCount += lvlData.conceptCount;
        }

        data[data.length - 1][0] = "Totals";
        data[data.length - 1][1] = new Integer(totalAreaCount);
        data[data.length - 1][2] = new Integer(totalPAreaCount);
        data[data.length - 1][3] = new Integer(totalConceptCount);
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
