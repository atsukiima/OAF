
package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.HashSet;
import java.util.Set;

/**
 * A target AbN retriever class for retrieving the set of attribute relationships 
 * (stored as Inheritable properties) used to define concepts in a 
 * given subhierarchy of concepts.
 * 
 * @author Chris O
 */
public class SCTInheritablePropertyRetriever implements TargetAbNDerivationWizardPanel.InheritablePropertyRetriever {
    
    private final SCTReleaseWithStated release;
    
    public SCTInheritablePropertyRetriever(SCTReleaseWithStated release) {
        this.release = release;
    }

    @Override
    public Set<InheritableProperty> getInheritablePropertiesInSubhierarchy(Concept root) {
        
        Set<InheritableProperty> propertyTypes = new HashSet<>();
        
        release.getConceptHierarchy().getSubhierarchyRootedAt((SCTConcept)root).getNodes().forEach( (concept) -> {
            SCTStatedConcept statedConcept = (SCTStatedConcept)concept;
            
            statedConcept.getStatedRelationships().forEach( (rel) -> {
                propertyTypes.add(new SCTInheritableProperty(rel.getType(), 
                        InheritableProperty.InheritanceType.Introduced));
            });
        });
        
        return propertyTypes;
    }
}