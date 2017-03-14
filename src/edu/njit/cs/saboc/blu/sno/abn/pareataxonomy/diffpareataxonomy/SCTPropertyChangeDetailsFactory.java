package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.PropertyChangeDetailsFactory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Factory method for computing change information about changes to 
 * attribute relationships. Currently uses the stated attribute relationships
 * of each concept for computing changes.
 * 
 * @author Chris O
 */
public class SCTPropertyChangeDetailsFactory implements PropertyChangeDetailsFactory {
    
    private final PAreaTaxonomy fromTaxonomy;
    private final PAreaTaxonomy toTaxonomy;
    
    private final Map<SCTInheritableProperty, Set<SCTConcept>> fromDomains = new HashMap<>();
    private final Map<SCTInheritableProperty, Set<SCTConcept>> toDomains = new HashMap<>();

    public SCTPropertyChangeDetailsFactory(
            SCTRelease fromRelease,
            SCTRelease toRelease,
            PAreaTaxonomy fromTaxonomy, 
            PAreaTaxonomy toTaxonomy) {
        
        this.fromTaxonomy = fromTaxonomy;
        this.toTaxonomy = toTaxonomy;
        
        initializeStatedAttributeRelationships(fromRelease, toRelease, fromTaxonomy, toTaxonomy);
    }

    @Override
    public Set<InheritableProperty> getFromOntProperties() {
        return fromTaxonomy.getPropertiesInTaxonomy();
    }

    @Override
    public Set<InheritableProperty> getToOntProperties() {
        return toTaxonomy.getPropertiesInTaxonomy();
    }

    @Override
    public Map<InheritableProperty, Set<Concept>> getFromDomains() {
        return (Map<InheritableProperty, Set<Concept>>)(Map<?, ?>)fromDomains;
    }

    @Override
    public Map<InheritableProperty, Set<Concept>> getToDomains() {
        return (Map<InheritableProperty, Set<Concept>>)(Map<?, ?>)toDomains;
    }
    
    /**
     * Determine which concepts are modeled with a given type of 
     * attribute relationship
     * 
     * @param fromRelease
     * @param toRelease
     * @param fromTaxonomy
     * @param toTaxonomy 
     */
    private void initializeStatedAttributeRelationships(
            SCTRelease fromRelease,
            SCTRelease toRelease,
            PAreaTaxonomy fromTaxonomy, 
            PAreaTaxonomy toTaxonomy) {

        if (fromRelease.supportsStatedRelationships() && toRelease.supportsStatedRelationships()) {
            
            Set<SCTConcept> fromConcepts = fromTaxonomy.getSourceHierarchy().getNodes();
            Set<SCTConcept> toConcepts = toTaxonomy.getSourceHierarchy().getNodes();

            fromConcepts.forEach( (concept) -> {
                
                SCTStatedConcept statedConcept = (SCTStatedConcept)concept;
                
                statedConcept.getStatedRelationships().forEach((statedRel) -> {
                    SCTInheritableProperty property = new SCTInheritableProperty(statedRel.getType(), InheritanceType.Introduced);

                    if (!fromDomains.containsKey(property)) {
                        fromDomains.put(property, new HashSet<>());
                    }

                    fromDomains.get(property).add(concept);
                });
            });

            toConcepts.forEach((concept) -> {
                SCTStatedConcept statedConcept = (SCTStatedConcept) concept;

                statedConcept.getStatedRelationships().forEach((statedRel) -> {
                    SCTInheritableProperty property = new SCTInheritableProperty(statedRel.getType(), InheritanceType.Introduced);

                    if (!toDomains.containsKey(property)) {
                        toDomains.put(property, new HashSet<>());
                    }

                    toDomains.get(property).add(concept);
                });
            });
        }
    }
}
