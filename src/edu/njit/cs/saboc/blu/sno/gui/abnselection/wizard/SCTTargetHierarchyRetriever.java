package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel.TargetHierarchyRetriever;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.HashSet;
import java.util.Set;

/**
 * Target AbN retriever class for obtaining the subhierarchy of concepts that are 
 * targets (or ancestors of targets) for a given type of attribute relationship
 * 
 * @author Chris O
 */
public class SCTTargetHierarchyRetriever implements TargetHierarchyRetriever {
    private final SCTReleaseWithStated release;
    
    public SCTTargetHierarchyRetriever(SCTReleaseWithStated release) {
        this.release = release;
    }

    @Override
    public Hierarchy<Concept> getTargetSubhierarchy(Concept root, InheritableProperty propertyType) {
        
        Set<SCTConcept> targetConcepts = new HashSet<>();
        
        release.getConceptHierarchy().getSubhierarchyRootedAt(
                (SCTConcept) root).getNodes().forEach((concept) -> {

                    SCTStatedConcept statedConcept = (SCTStatedConcept) concept;

                    statedConcept.getStatedRelationships().stream().filter((statedRel) -> {
                        return statedRel.getType().equals(propertyType.getPropertyType());
                    }).forEach((statedRel) -> {
                        targetConcepts.add(statedRel.getTarget());
                    });
                });
        
        Hierarchy<SCTConcept> targetHierarchy = release.getConceptHierarchy().getAncestorHierarchy(targetConcepts);
        
        return (Hierarchy<Concept>)(Hierarchy<?>)targetHierarchy;
    }
}