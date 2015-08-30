package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
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

    private String[] columnNames = new String[]{"Level", "Areas", "Partial-areas", "Concepts", "Rels"};
    private Object[][] data;

    private class LevelData {
        public int areaCount = 0;
        public int relCount = 0;
        public int pareaCount = 0;
        public int conceptCount = 0;
        
        public int aggregatePAreas = 0;
    }

    public PAreaLevelTableModel(SCTPAreaTaxonomy pareaTaxonomy) {
        
        if(pareaTaxonomy.isReduced()) {
            columnNames = new String[]{"Level", "Areas", "Partial-areas", "Concepts", "Abstraction Ratio", "Aggregate Partial-areas", "Rels"};
        } else {
            columnNames = new String[]{"Level", "Areas", "Partial-areas", "Concepts", "Abstraction Ratio", "Rels"};
        }
        
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

            ArrayList<SCTPArea> areaPAreas = a.getAllPAreas();
            
            level.areaCount++;
            level.pareaCount += areaPAreas.size();
            
            if(pareaTaxonomy.isReduced()) {
                for(SCTPArea parea : areaPAreas) {
                    SCTAggregatePArea reducedPArea = (SCTAggregatePArea)parea;
                    
                    if(reducedPArea.getAggregatedGroups().size() - 1 > 0) {
                         level.aggregatePAreas++;
                    }
                }
            }

            levelPAreas.addAll(a.getAllPAreas());

            lastArea = a;
        }

        level.relCount = lastArea.getRelationships().size();
        level.conceptCount = pareaTaxonomy.getDataSource().getConceptCountInPAreaHierarchy(
                        pareaTaxonomy, levelPAreas);

        levelData.add(level);

        data = new Object[levelData.size() + 1][columnNames.length];

        for (int r = 0; r < levelData.size(); r++) {
            data[r][0] = r;
            data[r][1] = levelData.get(r).areaCount;
            data[r][2] = levelData.get(r).pareaCount;
            data[r][3] = levelData.get(r).conceptCount;
            data[r][4] = (double)levelData.get(r).conceptCount / (double)levelData.get(r).pareaCount;
            
            if(pareaTaxonomy.isReduced()) {
                data[r][5] = levelData.get(r).aggregatePAreas;
                data[r][6] = levelData.get(r).relCount;
            } else {
                data[r][5] = levelData.get(r).relCount;
            }
            
        }

        int totalAreaCount = 0;
        int totalPAreaCount = 0;
        int totalConceptCount = 0;
        int totalAggregatePAreas = 0;

        for(LevelData lvlData : levelData) {
            totalAreaCount += lvlData.areaCount;
            totalPAreaCount += lvlData.pareaCount;
            totalConceptCount += lvlData.conceptCount;
            totalAggregatePAreas += lvlData.aggregatePAreas;
        }

        data[data.length - 1][0] = "Totals";
        data[data.length - 1][1] = totalAreaCount;
        data[data.length - 1][2] = totalPAreaCount;
        data[data.length - 1][3] = totalConceptCount;
        data[data.length - 1][4] = (double)totalConceptCount / (double)totalPAreaCount;
        
        if(pareaTaxonomy.isReduced()) {
            data[data.length - 1][5] = totalAggregatePAreas;
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
