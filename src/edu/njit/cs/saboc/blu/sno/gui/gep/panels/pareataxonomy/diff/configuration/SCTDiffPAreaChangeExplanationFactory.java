package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.abn.diff.explain.ConceptHierarchicalChange;
import edu.njit.cs.saboc.blu.core.abn.diff.explain.DiffAbNConceptChange;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.InheritablePropertyChange;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.InheritablePropertyDomainChange;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.InheritablePropertyHierarchyChange;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.diff.HierarchicalChangeExplanationFactory;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.diff.ChangeExplanationRowEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.diff.DiffNodeRootChangeExplanationModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.configuration.DiffPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaChangeExplanationFactory implements DiffNodeRootChangeExplanationModel.ChangeExplanationRowEntryFactory {

    private final DiffPAreaTaxonomyConfiguration config;

    private final HierarchicalChangeExplanationFactory hierarchicalChangeFactory;

    public SCTDiffPAreaChangeExplanationFactory(DiffPAreaTaxonomyConfiguration config) {
        this.config = config;

        this.hierarchicalChangeFactory = new HierarchicalChangeExplanationFactory(config);
    }

    @Override
    public ChangeExplanationRowEntry getChangeEntry(DiffAbNConceptChange item) {

        if (item instanceof ConceptHierarchicalChange) {
            return hierarchicalChangeFactory.getChangeEntry(item);
        } else if (item instanceof InheritablePropertyChange) {

            DiffAbNConceptChange.ChangeInheritanceType inheritance = item.getChangeInheritanceType();

            String inheritanceTypeStr;

            if (inheritance == DiffAbNConceptChange.ChangeInheritanceType.Direct) {
                inheritanceTypeStr = ChangeExplanationRowEntry.DIRECT;
            } else {
                inheritanceTypeStr = ChangeExplanationRowEntry.INDIRECT;
            }

            String changeTypeStr = "[UNKNOWN CHANGE TYPE]";
            String changeSummaryStr = "[NO SUMMARY SET]";
            String changeDescriptionStr = "";

            if (item instanceof InheritablePropertyDomainChange) {

            } else if (item instanceof InheritablePropertyHierarchyChange) {

            }

            
        }
        
        return new ChangeExplanationRowEntry("[UNSET]", "[UNSET]", "[UNSET]", "[UNSET]");
    }
}
