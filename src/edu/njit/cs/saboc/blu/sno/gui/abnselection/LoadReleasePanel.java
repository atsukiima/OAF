package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.sno.localdatasource.load.LoadLocalRelease;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LocalLoadStateMonitor;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RF1ReleaseLoader;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RF2ReleaseLoader;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.prefs.Preferences;
import javax.swing.*;

/**
 * Panel that allows a user to open a SNOMED CT release.
 * 
 * @author Chris O
 */
public class LoadReleasePanel extends JPanel {
    
    /**
     * Listener for handling events related to loading a SNOMED CT release
     */
    public interface LocalDataSourceListener {
        public void localDataSourceLoaded(SCTRelease dataSource);
        
        public void dataSourceLoading();

        public void localDataSourceUnloaded();
    }

    /**
     * A worker for monitoring the progress in loading a SNOMED CT release
     */
    private class LoadMonitorTask extends SwingWorker {

        private final LocalLoadStateMonitor stateMonitor;

        public LoadMonitorTask(LocalLoadStateMonitor stateMonitor) {
            this.stateMonitor = stateMonitor;
        }

        @Override
        public Void doInBackground() {

            setProgress(0);

            while (stateMonitor.getOverallProgress() < 100) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {

                }

                setProgress(stateMonitor.getOverallProgress());
            }

            return null;
        }
    }

    private JComboBox chooserVersionBox;
    private JComboBox recentlyOpenedVersionBox;
    private JComboBox localVersionBox = chooserVersionBox;

    private ArrayList<File> chooserReleases = new ArrayList<>();
    private ArrayList<File> recentlyOpenedReleases = new ArrayList<>();
    private ArrayList<File> availableReleases = chooserReleases;

    private final JButton chooserBtn;

    private SCTRelease loadedDataSource = null;

    private JToggleButton loadButton = new JToggleButton("Load");

    private JProgressBar loadProgressBar;

    private final ArrayList<LocalDataSourceListener> dataSourceLoadedListeners = new ArrayList<>();
    
    private String prefsKey_RecentlyOpenedReleases = "Recently Opened Releases";
    private String defaultJSON = "[]";

    public LoadReleasePanel() {
        chooserVersionBox = new JComboBox();
        chooserVersionBox.setBackground(Color.WHITE);
        chooserVersionBox.addItem("Choose a directory");
        chooserVersionBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                localVersionBox = chooserVersionBox;
                availableReleases = chooserReleases;
                loadButton.setText("Load from File Opener");
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        recentlyOpenedVersionBox = new JComboBox();
        recentlyOpenedVersionBox.setBackground(Color.WHITE);
        loadHistory();
        recentlyOpenedVersionBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                localVersionBox = recentlyOpenedVersionBox;
                availableReleases = recentlyOpenedReleases;
                loadButton.setText("Load from History");
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        chooserBtn = new JButton("Open Folder");

        chooserBtn.addActionListener((ae) -> {
            showReleaseFolderSelectionDialog();
        });

        loadButton.addActionListener( (ae) -> {
            
            if (loadButton.isSelected()) {
                
                if (availableReleases.isEmpty()) {
                    loadButton.setSelected(false);
                    return;
                }

                loadProgressBar.setValue(0);
                loadProgressBar.setString("Detecting Files...");
                loadProgressBar.setStringPainted(true);
                loadProgressBar.setVisible(true);
                
                loadButton.setEnabled(false);
                chooserVersionBox.setEnabled(false);
                recentlyOpenedVersionBox.setEnabled(false);
                chooserBtn.setEnabled(false);
                
                dataSourceLoadedListeners.forEach( (listener) -> {
                   listener.dataSourceLoading();
                });
                
                startLocalReleaseThread();
            } else {
                dataSourceUnloaded();
            }
        });

        loadProgressBar = new JProgressBar(0, 100);
        loadProgressBar.setVisible(false);

        JPanel localReleasePanel = new JPanel();

        localReleasePanel.add(chooserBtn);
        localReleasePanel.add(chooserVersionBox);
        localReleasePanel.add(new JLabel(" OR "));
        localReleasePanel.add(recentlyOpenedVersionBox);
        localReleasePanel.add(loadButton);
        localReleasePanel.add(loadProgressBar);

        this.setLayout(new BorderLayout());
        this.add(localReleasePanel);
    }

    public void addLocalDataSourceLoadedListener(LocalDataSourceListener listener) {
        dataSourceLoadedListeners.add(listener);
    }

    private void loadComplete() {
        loadButton.setText("Unload");
        loadButton.setEnabled(true);

        loadProgressBar.setVisible(false);

        dataSourceLoadedListeners.forEach((LocalDataSourceListener listener) -> {
            listener.localDataSourceLoaded(loadedDataSource);
        });
    }

    private void dataSourceUnloaded() {
        loadButton.setText("Load");
        localVersionBox.setEnabled(true);
        recentlyOpenedVersionBox.setEnabled(true);
        chooserBtn.setEnabled(true);

        this.loadedDataSource = null;

        dataSourceLoadedListeners.forEach( (listener) -> {
            listener.localDataSourceUnloaded();
        });
        loadHistory();
    }

    private void startLocalReleaseThread() {
        new Thread(() -> {
            try {
                File selectedFile = getSelectedVersion();

                saveHistory(selectedFile);

                final LocalLoadStateMonitor loadMonitor;
                final SCTRelease dataSource;
                
                // Determine which release format the release is in.
                // Currently do this based on the name of the path
                // where the release is located.
                if (selectedFile.getAbsolutePath().contains("RF2Release") || 
                        selectedFile.getAbsolutePath().contains("RF2_Production")) {
                    
                    RF2ReleaseLoader rf2Importer = new RF2ReleaseLoader();
                    
                    loadMonitor = rf2Importer.getLoadStateMonitor();
                    
                    LoadMonitorTask task = new LoadMonitorTask(loadMonitor);
                    
                    task.addPropertyChangeListener( (pce) -> {
                        
                        loadProgressBar.setValue(loadMonitor.getOverallProgress());
                        loadProgressBar.setString(loadMonitor.getProcessName());
                    });
                    
                    task.execute();
                    
                    dataSource = rf2Importer.loadLocalSnomedRelease(selectedFile, 
                            new SCTReleaseInfo(selectedFile, getSelectedVersionName()), 
                            loadMonitor);
                    
                    
                } else {
                    RF1ReleaseLoader importer = new RF1ReleaseLoader();
                    
                    loadMonitor = importer.getLoadStateMonitor();
                    
                    LoadMonitorTask task = new LoadMonitorTask(loadMonitor);
                    
                    task.addPropertyChangeListener((pce) -> {
                        loadProgressBar.setValue(loadMonitor.getOverallProgress());
                        loadProgressBar.setString(loadMonitor.getProcessName());
                    });
                    
                    task.execute();
                    
                    dataSource = importer.loadLocalSnomedRelease(selectedFile, 
                            new SCTReleaseInfo(selectedFile, getSelectedVersionName()), 
                            loadMonitor);
                }
                
                loadedDataSource = dataSource;
                
                SwingUtilities.invokeLater(() -> {
                    loadComplete();
                });
                
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();
    }

    /**
     * Opens a dialog that allows a user to select a folder that contains one 
     * or more SNOMED CT release folders.
     */
    private void showReleaseFolderSelectionDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                ArrayList<File> temp = LoadLocalRelease.findReleaseFolders(file);

                if(temp.isEmpty()){
                    JOptionPane.showMessageDialog(
                            null,
                            "SNOMED CT release was not found in the selected directory",
                            "SNOMED CT No Release Found",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    chooserVersionBox.removeAllItems();
                    chooserReleases = temp;
                    ArrayList<String> releaseNames = LoadLocalRelease.getReleaseFileNames(this.chooserReleases);

                    releaseNames.forEach((releaseName) -> {
                        chooserVersionBox.addItem(releaseName);
                    });
                    
                    localVersionBox = chooserVersionBox;
                    availableReleases = chooserReleases;
                    loadButton.setText("Load from File Opener");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "SNOMED CT release name cannot be properly parsed",
                        "SNOMED CT Release Name Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public File getSelectedVersion() {
        return availableReleases.get(localVersionBox.getSelectedIndex());
    }

    public String getSelectedVersionName() {
        return (String) localVersionBox.getItemAt(localVersionBox.getSelectedIndex());
    }

    public SCTRelease getLoadedDataSource() throws NoSCTDataSourceLoadedException {
        if (loadedDataSource == null) {
            throw new NoSCTDataSourceLoadedException();
        }

        return loadedDataSource;
    }
    
    private ArrayList<String> getRecentlyOpened(Class<?> c, String prefsKey) throws ParseException {
        ArrayList<String> returnList = new ArrayList<>();
        Preferences prefsRoot = Preferences.userNodeForPackage(c);
        String recentlyOpenedReleasesListJSON = prefsRoot.get(prefsKey,defaultJSON);
        System.out.println(recentlyOpenedReleasesListJSON);
        JSONParser parser = new JSONParser();
        JSONArray recentlyOpenedReleasesJSONArr = (JSONArray) parser.parse(recentlyOpenedReleasesListJSON);
        HashMap<Long,ArrayList<String>> unsorted = new HashMap<>(recentlyOpenedReleasesJSONArr.size());
        for (int i = 0; i < recentlyOpenedReleasesJSONArr.size(); i++){
            JSONObject obj = (JSONObject) recentlyOpenedReleasesJSONArr.get(i);
            long time = Long.parseLong((String) obj.get("time"));
            String path = (String) obj.get("data");
            if(unsorted.containsKey(time)){
                unsorted.get(time).add(path);
            }
            else{
                ArrayList<String> temp = new ArrayList<>();
                temp.add(path);
                unsorted.put(time,temp);
            }
        }
        List<Map.Entry<Long,ArrayList<String>>> sortedEntries = new ArrayList<>(unsorted.entrySet());
        Collections.sort(sortedEntries,
                (Map.Entry<Long,ArrayList<String>> e1, Map.Entry<Long,ArrayList<String>> e2) ->
                        e2.getKey().compareTo(e1.getKey())
        );
        for (int i = 0; i < sortedEntries.size(); i++){
            Map.Entry<Long,ArrayList<String>> entry = sortedEntries.get(i);
            for(String path : entry.getValue()){
                returnList.add(path);
            }
        }
        return returnList;
    }
    
    private ArrayList<File> getHistory(Class<?> c, String prefsKey) throws ParseException {
        ArrayList<String> stringArrayList = getRecentlyOpened(c,prefsKey);
        ArrayList<File> returnList = new ArrayList<>(stringArrayList.size());
        for(String path : stringArrayList){
            File temp = new File(path);
            try {
                if(temp.exists())
                    returnList.add(temp);
            } catch (SecurityException se) {
                se.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "One of the recently opened location is not readable - READ ACCESS DENIED."
                                + "\nLocation path: " + path
                                + "\nThis location is removed from the recently opened list.",
                        "File/Directory Read Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return returnList;
    }
    
    private void eraseHistory(Class<?> c, String prefsKey){
        Preferences prefsRoot = Preferences.userNodeForPackage(c);
        prefsRoot.remove(prefsKey);
    }
    
    private void loadHistory(){
        recentlyOpenedVersionBox.removeAllItems();
        recentlyOpenedReleases.clear();
        try{
            recentlyOpenedReleases = getHistory(this.getClass(),prefsKey_RecentlyOpenedReleases);
        }
        catch(ParseException pe) {
            pe.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Reading the list of recently opened files failed - PARSE ERROR",
                    "Recently Opened Files: Read Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch(ClassCastException|NumberFormatException e){
            e.printStackTrace();
            int answer = JOptionPane.showOptionDialog(
                    null,
                    "Reading the list of recently opened files failed - DATA ERROR"
                            + "\nHistory data seems corrupted.\nReset history?",
                    "Recently Opened Files: Read Failed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[]{"Yes","No"},
                    "No");
            switch(answer) {
                case 0:
                    eraseHistory(this.getClass(),prefsKey_RecentlyOpenedReleases);
                case 1:
                default:
            }
        }
        catch(Exception e){
            e.printStackTrace();
            int answer = JOptionPane.showOptionDialog(
                    null,
                    "Reading the list of recently opened files failed - UNKNOWN ERROR"
                            + "\nReset history?",
                    "Recently Opened Files: Read Failed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[]{"Yes","No"},
                    "No");
            switch(answer) {
                case 0:
                    eraseHistory(this.getClass(),prefsKey_RecentlyOpenedReleases);
                case 1:
                default:
            }
        }
        ArrayList<String> releaseNames = LoadLocalRelease.getReleaseFileNames(recentlyOpenedReleases);
        releaseNames.forEach((releaseName) -> {
            recentlyOpenedVersionBox.addItem(releaseName);
        });
        if(recentlyOpenedReleases.isEmpty()){
            recentlyOpenedVersionBox.addItem("NO History");
        }
    }
    
    private void saveHistory(File selectedFile){
        Preferences prefsRoot = Preferences.userNodeForPackage(this.getClass());
        String recentlyOpenedReleasesListJSON = prefsRoot.get(prefsKey_RecentlyOpenedReleases,defaultJSON);
        System.out.println(recentlyOpenedReleasesListJSON);
        HashMap<String,String> obj = new HashMap<>();
        obj.put("time",Long.toString(new Date().getTime()));
        obj.put("data",selectedFile.getAbsolutePath());
        JSONArray recentlyOpenedReleasesJSONArr;
        JSONParser parser = new JSONParser();
        try{
            recentlyOpenedReleasesJSONArr = (JSONArray) parser.parse(recentlyOpenedReleasesListJSON);
            recentlyOpenedReleasesJSONArr.add(obj);
            prefsRoot.put(prefsKey_RecentlyOpenedReleases, JSONValue.toJSONString(recentlyOpenedReleasesJSONArr));
        }catch(ParseException pe){
            pe.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Saving the list of recently opened files failed - PARSE ERROR",
                    "Recently Opened Files: Save Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
//    private void loadHistory(){
//        recentlyOpenedVersionBox.removeAllItems();
//        recentlyOpenedReleases.clear();
//        Preferences prefsRoot = Preferences.userNodeForPackage(this.getClass());
//        String recentlyOpenedReleasesListJSON = prefsRoot.get(prefsKey_RecentlyOpenedReleases,defaultJSON);
//        if(recentlyOpenedReleasesListJSON.equals(defaultJSON)){
//            recentlyOpenedVersionBox.addItem("NO History");
//            return;
//        }
//        JSONParser parser = new JSONParser();
//        try{
//            JSONArray recentlyOpenedReleasesJSONArr = (JSONArray) parser.parse(recentlyOpenedReleasesListJSON);
//            HashMap<Long,ArrayList<String>> unsorted = new HashMap<>(recentlyOpenedReleasesJSONArr.size());
//            for (int i = 0; i < recentlyOpenedReleasesJSONArr.size(); i++){
//                JSONObject obj = (JSONObject) recentlyOpenedReleasesJSONArr.get(i);
//                long time = Long.parseLong((String) obj.get("time"));
//                String path = (String) obj.get("data");
//                if(unsorted.containsKey(time)){
//                    unsorted.get(time).add(path);
//                }
//                else{
//                    ArrayList<String> temp = new ArrayList<>();
//                    temp.add(path);
//                    unsorted.put(time,temp);
//                }
//            }
//            List<Map.Entry<Long,ArrayList<String>>> sortedEntries = new ArrayList<>(unsorted.entrySet());
//            Collections.sort(sortedEntries,
//                    (Map.Entry<Long,ArrayList<String>> e1, Map.Entry<Long,ArrayList<String>> e2) ->
//                            e2.getKey().compareTo(e1.getKey())
//            );
//            for (int i = 0; i < sortedEntries.size(); i++){
//                Map.Entry<Long,ArrayList<String>> entry = sortedEntries.get(i);
//                for(String path : entry.getValue()){
//                    File temp = new File(path);
//                    try {
//                        if(temp.exists())
//                            recentlyOpenedReleases.add(temp);
//                    } catch (SecurityException se) {
//                        se.printStackTrace();
//                        JOptionPane.showMessageDialog(
//                                null,
//                                "One of the recently opened location is not readable - READ ACCESS DENIED."
//                                        + "\nLocation path: " + path
//                                        + "\nThis location is removed from the recently opened list.",
//                                "File/Directory Read Error",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }
//            ArrayList<String> releaseNames = LoadLocalRelease.getReleaseFileNames(recentlyOpenedReleases);
//            releaseNames.forEach((releaseName) -> {
//                recentlyOpenedVersionBox.addItem(releaseName);
//            });
//        }catch(ParseException pe){
//            pe.printStackTrace();
//            JOptionPane.showMessageDialog(
//                    null,
//                    "Reading the list of recently opened files failed - PARSE ERROR",
//                    "Recently Opened Files: Read Failed",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//        catch(ClassCastException|NumberFormatException e){
//            e.printStackTrace();
//            int answer = JOptionPane.showOptionDialog(
//                    null,
//                    "Reading the list of recently opened files failed - DATA ERROR"
//                            + "\nHistory data seems corrupted.\nReset history?",
//                    "Recently Opened Files: Read Failed",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.ERROR_MESSAGE,
//                    null,
//                    new Object[]{"Yes","No"},
//                    "No");
//            switch(answer) {
//                case 0:
//                    prefsRoot.remove(prefsKey_RecentlyOpenedReleases);
//                case 1:
//                default:
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            int answer = JOptionPane.showOptionDialog(
//                    null,
//                    "Reading the list of recently opened files failed - UNKNOWN ERROR"
//                            + "\nReset history?",
//                    "Recently Opened Files: Read Failed",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.ERROR_MESSAGE,
//                    null,
//                    new Object[]{"Yes","No"},
//                    "No");
//            switch(answer) {
//                case 0:
//                    prefsRoot.remove(prefsKey_RecentlyOpenedReleases);
//                case 1:
//                default:
//            }
//        }
//        if(recentlyOpenedReleases.isEmpty()){
//            recentlyOpenedVersionBox.addItem("NO History");
//        }
//    }
//
//    private void saveHistory(File selectedFile){
//        Preferences prefsRoot = Preferences.userNodeForPackage(this.getClass());
//        String recentlyOpenedReleasesListJSON = prefsRoot.get(prefsKey_RecentlyOpenedReleases,defaultJSON);
//        HashMap<String,String> obj = new HashMap<>();
//        obj.put("time",Long.toString(new Date().getTime()));
//        obj.put("data",selectedFile.getAbsolutePath());
//        JSONArray recentlyOpenedReleasesJSONArr;
//        if(recentlyOpenedReleasesListJSON.equals(defaultJSON)){
//            recentlyOpenedReleasesJSONArr = new JSONArray();
//            recentlyOpenedReleasesJSONArr.add(obj);
//            prefsRoot.put(prefsKey_RecentlyOpenedReleases, JSONValue.toJSONString(recentlyOpenedReleasesJSONArr));
//            return;
//        }
//        JSONParser parser = new JSONParser();
//        try{
//            recentlyOpenedReleasesJSONArr = (JSONArray) parser.parse(recentlyOpenedReleasesListJSON);
//            recentlyOpenedReleasesJSONArr.add(obj);
//            prefsRoot.put(prefsKey_RecentlyOpenedReleases, JSONValue.toJSONString(recentlyOpenedReleasesJSONArr));
//        }catch(ParseException pe){
//            pe.printStackTrace();
//            JOptionPane.showMessageDialog(
//                    null,
//                    "Saving the list of recently opened files failed - PARSE ERROR",
//                    "Recently Opened Files: Save Failed",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
