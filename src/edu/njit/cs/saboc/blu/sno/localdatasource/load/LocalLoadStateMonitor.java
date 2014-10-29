package edu.njit.cs.saboc.blu.sno.localdatasource.load;

/**
 *
 * @author Chris
 */
public class LocalLoadStateMonitor {
    private String currentProcessName = "Initializing";
    
    private int overallProgress = 0;
    
    public LocalLoadStateMonitor() {
        
    }
    
    public void setCurrentProcess(String name, int overallProgress) {
        this.currentProcessName = name;
        this.overallProgress = overallProgress;
    }
    
    public void setOverallProgress(int overallProgress) {
        this.overallProgress = overallProgress;
    }
    
    public String getProcessName() {
        return currentProcessName;
    }
    
    public int getOverallProgress() {
        return overallProgress;
    }
}
