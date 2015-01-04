package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Chris
 */
public class ClusterLevelTableModel extends AbstractTableModel {

    String[] columnNames = new String[]{"Level", "Tribal Bands", "Clusters", "Concepts", "Tribes"};
    private Object[][] data;

    private class LevelData {
        public int bandCount = 0;
        public int tribeCount = 0;
        public int clusterCount = 0;
        public int conceptCount = 0;
    }

    public ClusterLevelTableModel(TribalAbstractionNetwork tribalAbN) {
        ArrayList<CommonOverlapSet> tribalBands = (ArrayList<CommonOverlapSet>)tribalAbN.getBands();

        Collections.sort(tribalBands, new Comparator<CommonOverlapSet>() {
            public int compare(CommonOverlapSet a, CommonOverlapSet b) {
                Integer aCount = a.getSetEntryPoints().size();
                Integer bCount = b.getSetEntryPoints().size();

                return aCount.compareTo(bCount);
            }
        });

        ArrayList<LevelData> levelData = new ArrayList<LevelData>();

        CommonOverlapSet lastBand = tribalBands.get(0);
        LevelData level = new LevelData();
        ArrayList<ClusterSummary> levelClusters = new ArrayList<ClusterSummary>();

        for(CommonOverlapSet a : tribalBands) {
            if(lastBand.getSetEntryPoints().size() != a.getSetEntryPoints().size()) {
                level.tribeCount = lastBand.getSetEntryPoints().size();
                
                level.conceptCount = tribalAbN.getDataSource().getConceptCountInClusterHierarchy(
                                        tribalAbN, levelClusters);

                levelData.add(level);
                level = new LevelData();
                levelClusters.clear();
            }

            level.bandCount += 1;
            level.clusterCount += a.getAllClusters().size();

            levelClusters.addAll(a.getAllClusters());

            lastBand = a;
        }

        level.tribeCount = lastBand.getSetEntryPoints().size();
        level.conceptCount = tribalAbN.getDataSource().getConceptCountInClusterHierarchy(
                                        tribalAbN, levelClusters);

        levelData.add(level);

        data = new Object[levelData.size() + 1][5];

        for (int r = 0; r < levelData.size(); r++) {
            data[r][0] = r + 2;
            data[r][1] = levelData.get(r).bandCount;
            data[r][2] = levelData.get(r).clusterCount;
            data[r][3] = levelData.get(r).conceptCount;
            data[r][4] = levelData.get(r).tribeCount;
        }

        int totalAreaCount = 0;
        int totalPAreaCount = 0;
        int totalConceptCount = 0;

        for(LevelData lvlData : levelData) {
            totalAreaCount += lvlData.bandCount;
            totalPAreaCount += lvlData.clusterCount;
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
