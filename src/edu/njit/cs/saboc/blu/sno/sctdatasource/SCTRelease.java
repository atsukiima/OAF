package edu.njit.cs.saboc.blu.sno.sctdatasource;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.ontology.Ontology;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    // TODO: This needs to go away and be replaced by something better.
    private final HashMap<Character, Integer> startingIndex = new HashMap<>();

    private final ArrayList<DescriptionEntry> descriptions;
    
    private final SCTReleaseInfo releaseInfo;
    
    public SCTRelease(
            SCTReleaseInfo releaseInfo,
            Hierarchy<SCTConcept> activeConceptHierarchy, 
            Set<SCTConcept> allConcepts) {
        
        super(activeConceptHierarchy);
        
        this.releaseInfo = releaseInfo;
                
        this.descriptions = new ArrayList<>();
        
        allConcepts.forEach( (concept) -> {
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
    
    public SCTReleaseInfo getReleaseInfo() {
        return releaseInfo;
    }
    
    @Override
    public Hierarchy<SCTConcept> getConceptHierarchy() {
        return super.getConceptHierarchy();
    }

    public Optional<SCTConcept> getConceptFromId(long id) {
        return Optional.ofNullable(concepts.get(id));
    }
    
    public Set<SCTConcept> getAllConcepts() {
        return concepts.values().stream().collect(Collectors.toSet());
    }
    
    public Set<SCTConcept> getActiveConcepts() {
        return concepts.values().stream().filter((concept) -> {
           return concept.isActive();
        }).collect(Collectors.toSet());
    }
    
    public Set<SCTConcept> getInactiveConcepts() {
        return concepts.values().stream().filter((concept) -> {
            return !concept.isActive();
        }).collect(Collectors.toSet());
    }
    
    public Set<SCTConcept> getPrimitiveConcepts() {
        return concepts.values().stream().filter((concept) -> {
            return concept.isPrimitive();
        }).collect(Collectors.toSet());
    }
    
    public Set<SCTConcept> getFullyDefinedConcepts() {
        return concepts.values().stream().filter((concept) -> {
            return !concept.isPrimitive();
        }).collect(Collectors.toSet());
    }

    public Set<SCTConcept> searchExact(String term) {
        
        term = term.toLowerCase();
        
        if (term.length() < 3) {
            return Collections.emptySet();
        }

        char firstChar = Character.toLowerCase(term.charAt(0));

        Set<SCTConcept> results = new HashSet<>();

        int startIndex;

        if (firstChar < 'a') {
            startIndex = 0;
        } else if (firstChar > 'z') {
            startIndex = startingIndex.get('z');
        } else {
            startIndex = startingIndex.get(firstChar);
        }
                
        // TODO: Replace with binary search...
        
        boolean withinIndexBounds = (firstChar >= 'a' && firstChar <= 'z');

        for (int c = startIndex; c < descriptions.size(); c++) {
            DescriptionEntry entry = descriptions.get(c);
            
            char descFirstChar = Character.toLowerCase(entry.description.getTerm().charAt(0));

            if (withinIndexBounds) {
                if (descFirstChar == firstChar) {
                    if (entry.description.getTerm().equalsIgnoreCase(term)) {

                        if (entry.concept.isActive()) {
                            results.add(entry.concept);
                        }
                    }
                }
            } else {
                if (firstChar < 'a') {
                    if (descFirstChar == 'a') {
                        break;
                    }
                }
            }
        }

        return results;
    }

    public Set<SCTConcept> searchStarting(String term) {
        term = term.toLowerCase();
        
        if (term.length() < 3) {
            return Collections.emptySet();
        }

        char firstChar = Character.toLowerCase(term.charAt(0));

        Set<SCTConcept> results = new HashSet<>();

        int startIndex;

        if (firstChar < 'a') {
            startIndex = 0;
        } else if (firstChar > 'z') {
            startIndex = startingIndex.get('z');
        } else {
            startIndex = startingIndex.get(firstChar);
        }

        for (int c = startIndex; c < descriptions.size(); c++) {
            DescriptionEntry entry = descriptions.get(c);
            
            char descFirstChar = Character.toLowerCase(entry.description.getTerm().charAt(0));

            if (firstChar >= 'a' && firstChar <= 'z') {
                if (descFirstChar == firstChar) {
                    if (entry.description.getTerm().toLowerCase().startsWith(term)) {
                        if (entry.concept.isActive()) {
                            results.add(entry.concept);
                        }
                    }
                } else {
                    break;
                }
            } else {
                if(firstChar < 'a') {
                    if(descFirstChar == 'a') {
                        break;
                    }
                }
            }
        }
        
        return results;
    }

    public Set<SCTConcept> searchAnywhere(String term) {

        term = term.toLowerCase();

        Set<SCTConcept> results = new HashSet<>();

        for (DescriptionEntry entry : descriptions) {
            if (entry.description.getTerm().toLowerCase().contains(term)) {
                if (entry.concept.isActive()) {
                    results.add(entry.concept);
                }
            }
        }

        return results;
    }

    public boolean supportsStatedRelationships() {
        return false;
    }
}
