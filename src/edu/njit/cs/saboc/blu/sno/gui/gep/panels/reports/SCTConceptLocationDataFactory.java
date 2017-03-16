package edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.ConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTConceptLocationDataFactory implements ConceptLocationDataFactory<SCTConcept> {
    
    private final Map<Long, SCTConcept> concepts;
    
    public SCTConceptLocationDataFactory(Set<SCTConcept> sourceConcepts) {
        concepts = new HashMap<>();
        
        sourceConcepts.forEach( (c) -> {
            concepts.put(c.getID(), c);
        });
    }

    @Override
    public Set<SCTConcept> getConceptsFromIds(ArrayList<String> ids) {
        
        Set<Long> conceptIds = new HashSet<>();
        
        ids.forEach( (id) -> {
            
            try {
                long conceptId = Long.parseLong(id);
                conceptIds.add(conceptId);
                
            } catch(NumberFormatException nfe) {
                
            }
        });
        
        Set<SCTConcept> resultConcepts = new HashSet<>();
        
        conceptIds.forEach( (id) -> {
            if(concepts.containsKey(id)) {
                resultConcepts.add(concepts.get(id));
            }
        });
        
        return resultConcepts;
    }
}
