package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.utils.comparators.ConceptNameComparator;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFRecentlyOpenedFileManager;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFRecentlyOpenedFileManager.RecentlyOpenedFileException;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.data.NATConceptSearchResult;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.workspace.NATWorkspaceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SNOMED CT-specific data source for the NAT concept browser
 * 
 * @author Chris O
 */
public class SCTConceptBrowserDataSource extends ConceptBrowserDataSource<SCTConcept> {
    
    private final SCTRelease theRelease;
    private final OAFStateFileManager stateFileManager;
    
    public SCTConceptBrowserDataSource(SCTRelease release, OAFStateFileManager stateFileManager) {
        super(release);
        
        this.theRelease = release;
        this.stateFileManager = stateFileManager;
    }
    
    public SCTRelease getRelease() {
        return theRelease;
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
                + "<font face='Arial' size = '3'>"
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
    public String getFocusConceptText(AuditSet<SCTConcept> auditSet, SCTConcept concept) {
        
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
                + "<font face='Arial' size = '3'>"
                + "<br>%s"
                + "<br>(%s) (%s) (%s)"
                + "<p><b>Descriptions</b><br>"
                + "%s", 
                concept.getName(), 
                concept.getIDAsString(),
                concept.isPrimitive() ? "Primitve" : "Fully Defined",
                concept.isActive() ? "Active" : "Retired",
                super.getStyledAuditStatusText(auditSet, concept),
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

    @Override
    public Set<SCTConcept> getConceptsFromIds(Set<String> idStrs) {
        
        Set<SCTConcept> concepts = new HashSet<>();
        
        idStrs.forEach( (idStr) -> {
            try {
                long id = Long.parseLong(idStr);
                
                Optional<SCTConcept> concept = theRelease.getConceptFromId(id);
                
                if(concept.isPresent()) {
                    concepts.add(concept.get());
                }
                
            } catch (NumberFormatException nfe) {
                
            }
        });
        
        return concepts;
    }

    @Override
    public String getOntologyID() {
        return theRelease.getReleaseInfo().getReleaseName();
    }

    @Override
    public Set<? extends InheritableProperty> getPropertiesFromIds(Set<String> idStrs) {
        Set<SCTInheritableProperty> properties = new HashSet<>();
        
        idStrs.forEach( (idStr) -> {
            
            try {
                long id = Long.parseLong(idStr);
                
                Optional<SCTConcept> relType = theRelease.getConceptFromId(id);
                
                if(relType.isPresent() ){
                     properties.add(new SCTInheritableProperty(relType.get(), InheritanceType.Inherited));
                }                
            } catch (NumberFormatException nfe) {
                
            }
            
        });
        
        return properties;
    }

    @Override
    public ArrayList<InheritableProperty> getAvailableProperties() {
        ArrayList<InheritableProperty> properties = new ArrayList<>();
        
        ArrayList<SCTConcept> attributeRels = new ArrayList<>(theRelease.getAvailableAttributeRelationships());
        attributeRels.sort(new ConceptNameComparator());
        
        attributeRels.forEach( (rel) -> {
            properties.add(new SCTInheritableProperty(rel, InheritanceType.Introduced));
        });
        
        properties.sort( (a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        return properties;
    }


    @Override
    public OAFRecentlyOpenedFileManager getRecentlyOpenedAuditSets() {
        
        if(this.stateFileManager != null) {
            try {
                
                return this.stateFileManager.getRecentlyOpenedAuditSets(
                        this.theRelease.getReleaseInfo().getReleaseDirectory());
                
            } catch(RecentlyOpenedFileException rofe) {
                
            }
        }
        
        return null;
    }

    @Override
    public OAFRecentlyOpenedFileManager getRecentlyOpenedWorkspaces() {
        
        if(this.stateFileManager != null) {
            try {
                
                return this.stateFileManager.getRecentNATWorkspaces(
                        this.theRelease.getReleaseInfo().getReleaseDirectory());
            
            } catch(RecentlyOpenedFileException rofe) {
                
            }
        }
        
        return null;
    }
}
