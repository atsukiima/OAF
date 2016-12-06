package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.InheritablePropertyChange;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.PropertyChangeDetailsFactory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTPropertyChangeDetailsFactory extends PropertyChangeDetailsFactory {
    
    private final PAreaTaxonomy fromTaxonomy;
    private final PAreaTaxonomy toTaxonomy;

    public SCTPropertyChangeDetailsFactory(PAreaTaxonomy fromTaxonomy, PAreaTaxonomy toTaxonomy) {
        this.fromTaxonomy = fromTaxonomy;
        this.toTaxonomy = toTaxonomy;
    }
    
    @Override
    public Set<InheritableProperty> getFromOntProperties() {
        return fromTaxonomy.getAreaTaxonomy().getPropertiesInTaxonomy();
    }

    @Override
    public Set<InheritableProperty> getToOntProperties() {
        return toTaxonomy.getAreaTaxonomy().getPropertiesInTaxonomy();
    }

    @Override
    public Set<InheritableProperty> getFromPropertiesFor(Concept c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<InheritableProperty> getToPropertiesFor(Concept c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Concept, Set<InheritablePropertyChange>> getPropertyChanges() {
        Set<SCTConcept> fromConcepts = fromTaxonomy.getSourceHierarchy().getNodes();
        Set<SCTConcept> toConcepts = toTaxonomy.getSourceHierarchy().getNodes();
        
        Map<InheritableProperty, Set<SCTConcept>> fromRelConcepts = new HashMap<>();
        Map<InheritableProperty, Set<SCTConcept>> toRelConcepts = new HashMap<>();
        
        fromConcepts.forEach((concept) -> {
            Set<InheritableProperty> fromRels = fromTaxonomy.getAreaTaxonomy().getPAreaTaxonomyFactory().getRelationships(concept);
            
            fromRels.forEach( (rel) -> {
                if(!fromRelConcepts.containsKey(rel)) {
                    fromRelConcepts.put(rel, new HashSet<>());
                }
                
                fromRelConcepts.get(rel).add(concept);
            });
        });
        
        toConcepts.forEach((concept) -> {
            Set<InheritableProperty> toRels = toTaxonomy.getAreaTaxonomy().getPAreaTaxonomyFactory().getRelationships(concept);

            toRels.forEach((rel) -> {
                if (!toRelConcepts.containsKey(rel)) {
                    toRelConcepts.put(rel, new HashSet<>());
                }

                toRelConcepts.get(rel).add(concept);
            });
        });
        
        return null;
    }
    
}
