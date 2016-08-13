package edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.ConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTConceptLocationDataFactory implements ConceptLocationDataFactory {
    
    private final Map<Long, Concept> concepts;
    
    public SCTConceptLocationDataFactory(Set<Concept> sourceConcepts) {
        concepts = new HashMap<>();
        
        sourceConcepts.forEach( (c) -> {
            concepts.put((Long)c.getID(), c);
        });
    }

    @Override
    public Set<Concept> getConceptsFromIds(ArrayList<String> ids) {
        
        Set<Long> conceptIds = new HashSet<>();
        
        ids.forEach( (id) -> {
            conceptIds.add(Long.parseLong(id));
        });
        
        Set<Concept> resultConcepts = new HashSet<>();
        
        conceptIds.forEach( (id) -> {
            if(concepts.containsKey(id)) {
                resultConcepts.add(concepts.get(id));
            }
        });
        
        return resultConcepts;
    }
}
