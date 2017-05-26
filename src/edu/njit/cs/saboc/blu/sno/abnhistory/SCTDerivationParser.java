package edu.njit.cs.saboc.blu.sno.abnhistory;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationParser;
import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivation;
import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;
import edu.njit.cs.saboc.blu.core.abn.targetbased.provenance.TargetAbNDerivation;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTStatedRelationshipsPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance.SCTBasicStatedPAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTStatedTANFactory;
import edu.njit.cs.saboc.blu.sno.abn.tan.provenance.SCTStatedTANDerivation;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTPropertyLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author hl395
 */
public class SCTDerivationParser extends AbNDerivationParser {
        
    private final SCTRelease release;

    public SCTDerivationParser(SCTRelease release){
        
        super(release, 
                new SCTConceptLocationDataFactory(release.getActiveConcepts()), 
                new SCTPropertyLocationDataFactory(release.getActiveConcepts()),
                new SCTAbNDerivationFactory(release));
        
        this.release = release;
    }

    @Override
    public AbNDerivation parseDerivationHistory(JSONObject obj) throws AbNParseException {
        if(!obj.containsKey("ClassName")) {
            throw new AbNParseException("Derivation history type not specified.");
        }
        
        String className = obj.get("ClassName").toString();
        
        if (className.equalsIgnoreCase("SCTBasicStatedPAreaTaxonomyDerivation")) {
            return parseSCTBasicStatedPAreaTaxonomyDerivation(obj);
        } else if (className.equalsIgnoreCase("SCTStatedTANDerivation")) {
            return parseSCTStatedTANDerivation(obj);
        } else {
            return super.parseDerivationHistory(obj);
        }
    }

    @Override
    public SimplePAreaTaxonomyDerivation parseSimplePAreaTaxonomyDerivation(JSONObject obj) throws AbNParseException {   
        
        return super.parseSimplePAreaTaxonomyDerivation(
                obj, 
                new SCTInferredPAreaTaxonomyFactory(release,
                        release.getConceptHierarchy()));
        
    }

    public SCTBasicStatedPAreaTaxonomyDerivation parseSCTBasicStatedPAreaTaxonomyDerivation(JSONObject obj) throws AbNParseException {
        
        if (!release.supportsStatedRelationships()) {
            throw new AbNParseException("Stated relationships not supported with selected SCT release.");
        }
        
        SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)release;
        
        SimplePAreaTaxonomyDerivation baseDerivation = super.parseSimplePAreaTaxonomyDerivation(obj);
        
        baseDerivation = super.parseSimplePAreaTaxonomyDerivation(obj, 
                new SCTStatedRelationshipsPAreaTaxonomyFactory(
                    statedRelease, 
                    (SCTConcept)baseDerivation.getRoot()));
        
        return new SCTBasicStatedPAreaTaxonomyDerivation(baseDerivation);
    }

    public SCTStatedTANDerivation parseSCTStatedTANDerivation(JSONObject obj) throws AbNParseException  {
        
        if(!release.supportsStatedRelationships()) {
            throw new AbNParseException("Stated relationships not supported with selected SCT release.");
        }
        
        SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)release;
        
        SimpleClusterTANDerivation baseDerivation = super.parseSimpleClusterTANDerivation(obj, new SCTStatedTANFactory(statedRelease));
        
        return new SCTStatedTANDerivation(baseDerivation);
    }

    @Override
    public TargetAbNDerivation parseTargetAbNDerivation(JSONObject obj) throws AbNParseException {
        
        if(!release.supportsStatedRelationships()) {
            throw new AbNParseException("Stated relationships not supported with selected SCT release.");
        }
        
        if (!obj.containsKey("PropertyID")) {
            throw new AbNParseException("Property ID not specified.");
        }

        Concept sourceHierarchyRoot = getRoot(obj, "SourceRootID");
        Concept targetHierarchyRoot = getRoot(obj, "TargetRootID");

        String propertyID = (String) obj.get("PropertyID");

        Set<InheritableProperty> property = super.getPropertyLocationFactory().getPropertiesFromIds(new ArrayList<>(Arrays.asList(propertyID)));

        if (property.isEmpty()) {
            throw new AbNParseException("Property with specified ID not found.");
        }

        Hierarchy<SCTConcept> sourceHierarchy = release.getConceptHierarchy().getSubhierarchyRootedAt((SCTConcept) sourceHierarchyRoot);
        Hierarchy<SCTConcept> targetHierarchy = release.getConceptHierarchy().getSubhierarchyRootedAt((SCTConcept) targetHierarchyRoot);

        SCTInheritableProperty owlProperty = (SCTInheritableProperty) property.iterator().next();

        SCTTargetAbstractionNetworkFactory factory = new SCTTargetAbstractionNetworkFactory(
                (SCTReleaseWithStated)release, 
                sourceHierarchy, 
                owlProperty,
                targetHierarchy);

        return new TargetAbNDerivation(
                factory,
                sourceHierarchyRoot,
                property.iterator().next(),
                targetHierarchyRoot);
    }
}
