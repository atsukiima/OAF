package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.data.NATConceptSearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Chris O
 */
public class SCTConceptBrowserDataSource extends ConceptBrowserDataSource<SCTConcept> {
    
    private final SCTRelease theRelease;
    
    public SCTConceptBrowserDataSource(SCTRelease release) {
        super(release);
        
        this.theRelease = release;
    }
    
    @Override
    public String getFocusConceptText(SCTConcept concept) {
        
        ArrayList<String> descriptions = new ArrayList<>();
        
        concept.getDescriptions().forEach( (description) -> {
            descriptions.add(description.getTerm());
        });
        
        Collections.sort(descriptions);
        
        String descStr = "";
        
        for(String desc : descriptions) {
            descStr += ("- " + desc + "<br>");
        }
        
        return String.format("<html><font face='Arial' size = '5'>"
                + "<b>%s</b></font>"
                + "<font face='Arial' size = '3'"
                + "<br>%s"
                + "<br>(%s) (%s)"
                + "<p><b>Descriptions</b><br>"
                + "%s", 
                concept.getName(), 
                concept.getIDAsString(),
                concept.isPrimitive() ? "Primitve" : "Fully Defined",
                concept.isActive() ? "Active" : "Retired",
                descStr);
    }

    @Override
    public ArrayList<NATConceptSearchResult<SCTConcept>> searchExact(String str) {
        ArrayList<SCTConcept> concepts = new ArrayList<>(theRelease.searchExact(str));
        
        ArrayList<NATConceptSearchResult<SCTConcept>> results = new ArrayList<>();
        
        concepts.forEach( (concept) -> {
            
            Set<String> matchedTerms = concept.getDescriptions().stream().filter( (description) -> {
                return description.getTerm().equalsIgnoreCase(str.toLowerCase());
            }).map( (desc) -> desc.getTerm()).collect(Collectors.toSet());
            
            results.add(new NATConceptSearchResult<>(concept, matchedTerms, str));
        });
        
        
        results.sort( (a, b) -> {
            return a.getConcept().getName().compareToIgnoreCase(b.getConcept().getName());
        });
        
        return results;
    }

    @Override
    public ArrayList<NATConceptSearchResult<SCTConcept>> searchStarting(String str) {
        ArrayList<SCTConcept> concepts = new ArrayList<>(theRelease.searchStarting(str));
        
        ArrayList<NATConceptSearchResult<SCTConcept>> results = new ArrayList<>();
        
        concepts.forEach( (concept) -> {
            
            Set<String> matchedTerms = concept.getDescriptions().stream().filter( (description) -> {
                return description.getTerm().toLowerCase().startsWith(str.toLowerCase());
            }).map( (desc) -> desc.getTerm()).collect(Collectors.toSet());
            
            results.add(new NATConceptSearchResult<>(concept, matchedTerms, str));
        });
        
        
        results.sort( (a, b) -> {
            return a.getConcept().getName().compareToIgnoreCase(b.getConcept().getName());
        });
        
        return results;
    }

    @Override
    public ArrayList<NATConceptSearchResult<SCTConcept>> searchAnywhere(String str) {
        ArrayList<SCTConcept> concepts = new ArrayList<>(theRelease.searchAnywhere(str));
        
        ArrayList<NATConceptSearchResult<SCTConcept>> results = new ArrayList<>();
        
        concepts.forEach( (concept) -> {
            Set<String> matchedTerms = concept.getDescriptions().stream().filter( (description) -> {
                return description.getTerm().toLowerCase().contains(str.toLowerCase());
            }).map( (desc) -> desc.getTerm()).collect(Collectors.toSet());
            
            results.add(new NATConceptSearchResult<>(concept, matchedTerms, str));
        });
        
        
        results.sort( (a, b) -> {
            return a.getConcept().getName().compareToIgnoreCase(b.getConcept().getName());
        });
        
        return results;
    }

    @Override
    public ArrayList<NATConceptSearchResult<SCTConcept>> searchID(String str) {
        
        ArrayList<NATConceptSearchResult<SCTConcept>> results = new ArrayList<>();
        
        try {
            long id = Long.parseLong(str);
            
            Optional<SCTConcept> optConcept = theRelease.getConceptFromId(id);
            
            if(optConcept.isPresent()) {
                SCTConcept concept = optConcept.get();
                
                results.add(new NATConceptSearchResult<>(concept, Collections.singleton(concept.getIDAsString()), str));
            }
            
        } catch(NumberFormatException e) {
            
        }
        
        return results;
    }
}
