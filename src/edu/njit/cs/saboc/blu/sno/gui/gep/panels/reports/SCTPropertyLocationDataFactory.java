/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.PropertyLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Hao Liu
 */
public class SCTPropertyLocationDataFactory implements PropertyLocationDataFactory{

    private final Map<Long, SCTConcept> concepts;
    
    public SCTPropertyLocationDataFactory(Set<SCTConcept> sourceConcepts) {
        concepts = new HashMap<>();
        
        sourceConcepts.forEach( (c) -> {
            concepts.put(c.getID(), c);
        });
    }
    
    @Override
    public Set<InheritableProperty> getPropertiesFromIds(ArrayList<String> ids) {

         Set<Long> conceptIds = new HashSet<>();
        
        ids.forEach( (id) -> {
            conceptIds.add(Long.parseLong(id));
        });
        
        Set<InheritableProperty> resultPropertys = new HashSet<>();
        
        conceptIds.forEach( (id) -> {
            if(concepts.containsKey(id)) {
                SCTInheritableProperty sctProperty = new SCTInheritableProperty(
                        concepts.get(id), 
                        InheritableProperty.InheritanceType.Introduced);
                
                resultPropertys.add(sctProperty);
            }
        });
        
        return resultPropertys;

    }
}
