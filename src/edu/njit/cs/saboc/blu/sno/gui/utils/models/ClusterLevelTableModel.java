package edu.njit.cs.saboc.blu.sno.gui.utils.models;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
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

    public ClusterLevelTableModel(SCTTribalAbstractionNetwork tribalAbN) {
        ArrayList<SCTBand> tribalBands = (ArrayList<SCTBand>)tribalAbN.getBands();

        Collections.sort(tribalBands, new Comparator<SCTBand>() {
            public int compare(SCTBand a, SCTBand b) {
                Integer aCount = a.getPatriarchs().size();
                Integer bCount = b.getPatriarchs().size();

                return aCount.compareTo(bCount);
            }
        });

        ArrayList<LevelData> levelData = new ArrayList<LevelData>();

        SCTBand lastBand = tribalBands.get(0);
        LevelData level = new LevelData();
        ArrayList<SCTCluster> levelClusters = new ArrayList<>();

        for(SCTBand a : tribalBands) {
            if(lastBand.getPatriarchs().size() != a.getPatriarchs().size()) {
                level.tribeCount = lastBand.getPatriarchs().size();
                
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

        level.tribeCount = lastBand.getPatriarchs().size();
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
