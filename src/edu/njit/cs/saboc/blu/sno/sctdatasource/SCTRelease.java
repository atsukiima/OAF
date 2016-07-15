package edu.njit.cs.saboc.blu.sno.sctdatasource;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Ontology;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Chris O
 */
public class SCTRelease extends Ontology {

    private class DescriptionEntry {

        public Description description;
        public SCTConcept concept;

        public DescriptionEntry(Description d, SCTConcept c) {
            this.description = d;
            this.concept = c;
        }
    }

    private final Map<Long, SCTConcept> concepts = new HashMap<>();

    private final HashMap<Character, Integer> startingIndex = new HashMap<>();

    private final ArrayList<DescriptionEntry> descriptions;
    
    public SCTRelease(Hierarchy<SCTConcept> conceptHierarchy) {
        
        super(conceptHierarchy);
                
        descriptions = new ArrayList<>();
        
        conceptHierarchy.getNodes().forEach( (concept) -> {
            concepts.put(concept.getID(), concept);
            
            concept.getDescriptions().forEach((d) -> {
                descriptions.add(new DescriptionEntry(d, concept));
            });
        });

        Collections.sort(descriptions, (a, b) -> a.description.getTerm().compareToIgnoreCase(b.description.getTerm()));

        char lastChar = Character.toLowerCase(descriptions.get(0).description.getTerm().charAt(0));

        for (int c = 1; c < descriptions.size(); c++) {
            String term = descriptions.get(c).description.getTerm();

            char curChar = Character.toLowerCase(term.charAt(0));

            if (curChar != lastChar) {
                if (curChar >= 'a' && curChar <= 'z') {
                    startingIndex.put(curChar, c);
                }

                lastChar = curChar;
            }
        }
    }
    
    public Hierarchy<SCTConcept> getConceptHierarchy() {
        return super.getConceptHierarchy();
    }

    public SCTConcept getConceptFromId(long id) {
        return concepts.get(id);
    }

    public boolean supportsStatedRelationships() {
        return false;
    }
}
