/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.ddirules;

import java.util.*;

/**
 *
 * @author azakharchenko
 */
public class RuleObject 
{
    private String conceptname;
    private HashMap<String,String> conceptlist=new HashMap<String,String>();

    public RuleObject() 
    {
        this.conceptname = "";
    }
    
    public RuleObject(String conceptname) 
    {
        this.conceptname = conceptname;
    }

    public RuleObject(String conceptname, String newconceptname, String interaction) 
    {
        this.conceptname = conceptname;
        conceptlist.put(newconceptname, interaction);
    }

    public String getConceptname() 
    {
        return conceptname;
    }

    public void setConceptname(String conceptname) 
    {
        this.conceptname = conceptname;
    }

    public HashMap<String, String> getConceptlist() 
    {
        return conceptlist;
    }

    public void setConceptlist(HashMap<String, String> conceptlist) 
    {
        this.conceptlist = conceptlist;
    }
    public void addRule(String newconcept, String interaction)
    {
        conceptlist.put(newconcept, interaction);
    }
    public void removeRule(String oldconcept)
    {
        conceptlist.remove(oldconcept);
    }
    /**
     * Returns the total number of rules for this entry;
     * @return 
     */
    public int ruleNum()
    {
        return conceptlist.size();
    }
    @Override
    public String toString() 
    {
        StringBuilder result=new StringBuilder();
        for (Map.Entry entry : conceptlist.entrySet()) 
        {
            result.append(conceptname);
            result.append("|");
            result.append(entry.getKey());
            result.append("|");
            result.append(entry.getValue());
            result.append("\r\n");
        }
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 43 * hash + (this.conceptname != null ? this.conceptname.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final RuleObject other = (RuleObject) obj;
        return !((this.conceptname == null) ? (other.conceptname != null) : !this.conceptname.equals(other.conceptname));
    }
    
    
    
}
