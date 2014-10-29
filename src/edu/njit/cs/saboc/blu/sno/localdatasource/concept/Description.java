/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

/**
 *
 * @author Chris
 */
public class Description {
    
    byte [] term;

    private byte descriptionType;

    public Description(String term, int descriptionType) {
        
        try {
            this.term = term.getBytes("ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.descriptionType = (byte)descriptionType;
    }

    public String getTerm() {
        String s;
        
        try {
            s = new String(term, "ISO-8859-1");
        } catch(Exception e) {
            s = "";
        }
        
        return s;
    }
    
    public int getDescriptionType() {
        return descriptionType;
    }
}
