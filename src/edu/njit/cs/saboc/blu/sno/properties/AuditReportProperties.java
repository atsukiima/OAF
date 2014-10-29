/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.properties;

/**
 * Singleton class that manages the Audit Suggestions/Recommendations properties
 * @author harsh
 */
public class AuditReportProperties {
    
    private static AuditReportProperties properties = null;
    
    private boolean regionsOn = false;
    
    private int smallPArea = 6;
    private int manyRelationships = 2;
    private int fewSmallPAreas = 3;
    private boolean primitives = false;
    private boolean strictRegions = false;
    
    /**
     * Restrict Object instantiation by other objects.
     */
    private AuditReportProperties() {
        
    }
    
    /**
     * Return the singleton instance of properties.
     * @return 
     */
    public static AuditReportProperties getAuditReportProperties() {
        if(properties == null) {
            properties = new AuditReportProperties();
        }
        
        return properties;
    }
    
    /**
     * Set 'true' if the user has turned on Regions in Options dialog, 'false' otherwise
     * @param regionsOn 
     */
    public void setRegionsOn(boolean regionsOn) {
        this.regionsOn = regionsOn;
    }
    
    /**
     * Check if the user has turned on the Regions in Options dialog
     * @return 
     */
    public boolean isRegionsOn() {
        return this.regionsOn;
    }
    
    /**
     * Sets the magnitude that determines that a Partial-area is small
     * @param smallPArea 
     */
    public void setSmallPArea(int smallPArea) {
        this.smallPArea = smallPArea;
    }
    
    /**
     * Returns the magnitude that determines that a Partial-area is small
     * @return 
     */
    public int getSmallPArea() {
        return this.smallPArea;
    }
    
    /**
     * Sets the magnitude that determines that a Partial-area has many relationships
     * @param manyRelationships
     */
    public void setManyRelationships(int manyRelationships) {
        this.manyRelationships = manyRelationships;
    }
    
    /**
     * Returns the magnitude that determines that a Partial-area has many relationships
     * @return 
     */
    public int getManyRelationships() {
        return this.manyRelationships;
    }
    
    /**
     * Sets the magnitude that determines the limit for an Area to be classified as having
     * few small Partial-areas
     * @param manyRelationships
     */
    public void setFewSmallPAreas(int fewSmallPAreas) {
        this.fewSmallPAreas = fewSmallPAreas;
    }
    
    /**
     * Returns the magnitude that determines the limit for an Area to be classified as having
     * few small Partial-areas
     * @return 
     */
    public int getFewSmallPAreas() {
        return this.fewSmallPAreas;
    }
    
    /**
     * Sets whether the Concepts need to be displayed as "non-primitives first"
     * in Audit Recommendations
     * @param primitives
     */
    public void setPrimitivesLast(boolean primitives) {
        this.primitives = primitives;
    }
    
    /**
     * Returns whether Concepts need to be displayed as "non-primitives first"
     * in Audit Recommendations
     * @return 
     */
    public boolean getPrimitivesLast() {
        return this.primitives;
    }
    
    /**
     * Sets whether the Concepts need to be sorted based on strict inheritance/mixed/introduction
     * regions
     * @param strictRegions
     */
    public void setStrictRegionsProperty(boolean strictRegions) {
        this.strictRegions = strictRegions;
    }
    
    /**
     * Returns whether Concepts need to be sorted based on strict inheritance/mixed/introduction
     * regions
     * @return 
     */
    public boolean getStrictRegionsProperty() {
        return this.strictRegions;
    }
}
