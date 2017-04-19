/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationFactory;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbNFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTStatedTANFactory;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;

/**
 *
 * @author hl395
 */
public class SCTAbNDerivationFactory implements AbNDerivationFactory {
    
    private SCTRelease sctRelease;
    private SCTReleaseWithStated statedRelease;
    private Hierarchy<SCTConcept> hierarchy;

    @Override
    public PAreaTaxonomyFactory getPAreaTaxonomyFactory() {
        return new SCTInferredPAreaTaxonomyFactory(sctRelease, hierarchy);
    }

    @Override
    public DisjointAbNFactory getDisjointPAreaAbNFactory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DisjointAbNFactory getDisjointTANbNFactory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TANFactory getTANFactory() {
       return new TANFactory(statedRelease);
    }

    @Override
    public TargetAbstractionNetworkFactory getTargetAbNFactory() {
        return new SCTTargetAbstractionNetworkFactory(statedRelease, null, null, null);
    }
}
