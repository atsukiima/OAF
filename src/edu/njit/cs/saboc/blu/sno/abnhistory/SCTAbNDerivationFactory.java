package edu.njit.cs.saboc.blu.sno.abnhistory;

import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationParser;
import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationParser.AbNParseException;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author hl395
 */
public class SCTAbNDerivationFactory extends AbNDerivationFactory {
    
    private final SCTRelease release;
    
    public SCTAbNDerivationFactory(SCTRelease release) {
        this.release = release;
    }

    @Override
    public PAreaTaxonomyFactory getPAreaTaxonomyFactory() throws AbNParseException {
        throw new AbNDerivationParser.AbNParseException("SCT PAreaTaxonomy Factory not injectable.");
    }

    @Override
    public TANFactory getTANFactory() {
       return new TANFactory(release);
    }

    @Override
    public TargetAbstractionNetworkFactory getTargetAbNFactory() throws AbNParseException {
        throw new AbNParseException("SCT Target AbN Factory not injectable.");
    }
}
