package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateDisjointAbNFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.*;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateAncestorSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateExpandedSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateTargetAbNFromPAreaButton;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayPAreaTaxonomyAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends NodeOptionsPanel {

    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config, boolean aggregated) {
        
        SCTOpenBrowserButton openBrowserButton = new SCTOpenBrowserButton(
                config, 
                config.getRelease(),
                config.getUIConfiguration().getFrameManager());
        
        super.addOptionButton(openBrowserButton);

        if (aggregated) {
            CreateExpandedSubtaxonomyButton expandedSubtaxonomyBtn = new CreateExpandedSubtaxonomyButton(
                    config, new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));

            super.addOptionButton(expandedSubtaxonomyBtn);
        } else {
            CreateDisjointAbNFromSinglyRootedNodeButton<PArea> createDisjointBtn = new CreateDisjointAbNFromSinglyRootedNodeButton<>(
                    config,
                    (disjointTaxonomy) -> {
                        config.getUIConfiguration().getAbNDisplayManager().displayDisjointPAreaTaxonomy(disjointTaxonomy);
                    });

            super.addOptionButton(createDisjointBtn);
        }
        
        
        CreateRootSubtaxonomyButton rootSubtaxonomyBtn = new CreateRootSubtaxonomyButton(config,
            new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        
        CreateAncestorSubtaxonomyButton ancestorSubtaxonomyBtn = new CreateAncestorSubtaxonomyButton(config, 
            new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(ancestorSubtaxonomyBtn);
        
        
        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(
                new TANFactory(config.getRelease()),
                config, 
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
        
        
        SCTRelease release = config.getRelease();
        
        if (release.supportsStatedRelationships()) {
            CreateTargetAbNFromPAreaButton targetAbNBtn = new CreateTargetAbNFromPAreaButton() {

                @Override
                protected Set<InheritableProperty> getUsableProperties(PArea parea) {
                    return parea.getRelationships();
                }

                @Override
                protected void createAndDisplayTargetAbN(InheritableProperty property) {
                    
                    SCTInheritableProperty sctProperty = (SCTInheritableProperty)property;
                    
                    Set<SCTConcept> targets = new HashSet<>();
                    
                    PAreaTaxonomy sourceTaxonomy = config.getPAreaTaxonomy();
                    PArea parea = this.getCurrentEntity().get();
                    
                    Hierarchy<SCTConcept> sourceHierarchy = (Hierarchy<SCTConcept>) (Hierarchy<?>) parea.getHierarchy();

                    sourceHierarchy.getNodes().forEach((concept) -> {
                        concept.getAttributeRelationships().forEach((relationship) -> {
                            if (relationship.getType().equals(property.getPropertyType())) {
                                targets.add(relationship.getTarget());
                            }
                        });
                    });

                    SCTReleaseWithStated release = (SCTReleaseWithStated) config.getRelease();

                    Hierarchy<SCTConcept> targetHierarchy = release.getConceptHierarchy().getAncestorHierarchy(targets);

                    SCTTargetAbstractionNetworkFactory factory = new SCTTargetAbstractionNetworkFactory(
                            release,
                            sourceHierarchy,
                            sctProperty,
                            targetHierarchy);

                    TargetAbstractionNetworkGenerator generator = new TargetAbstractionNetworkGenerator();

                    TargetAbstractionNetwork<TargetGroup> targetAbN = generator.deriveTargetAbNFromPArea(
                            factory,
                            sourceTaxonomy,
                            parea,
                            sctProperty,
                            (Hierarchy<Concept>) (Hierarchy<?>) targetHierarchy);

                    config.getUIConfiguration().getAbNDisplayManager().displayTargetAbstractionNetwork(targetAbN);
                }
            };

            super.addOptionButton(targetAbNBtn);
        }
        
        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("partial-area", () -> {
            PArea parea = (PArea)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createNodeDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        
        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);
        
        super.addOptionButton(exportBtn);
        

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
