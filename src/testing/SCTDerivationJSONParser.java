/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivationParser;
import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivation;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance.SCTBasicStatedPAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.sno.abn.tan.provenance.SCTStatedTANDerivation;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTPropertyLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import org.json.simple.JSONObject;

/**
 *
 * @author hl395
 */
public class SCTDerivationJSONParser extends AbNDerivationParser {
        
    public SCTDerivationJSONParser(
            SCTRelease release, 
            SCTConceptLocationDataFactory conceptFactory, 
            SCTPropertyLocationDataFactory propertyFactory, 
            SCTAbNDerivationFactory abnFactory){
        
        super(release, 
                new SCTConceptLocationDataFactory(release.getActiveConcepts()), 
                new SCTPropertyLocationDataFactory(release.getActiveConcepts()),
                new SCTAbNDerivationFactory());
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

    public SCTBasicStatedPAreaTaxonomyDerivation parseSCTBasicStatedPAreaTaxonomyDerivation(JSONObject obj) throws AbNParseException {
        return null;
    }

    public SCTStatedTANDerivation parseSCTStatedTANDerivation(JSONObject obj) throws AbNParseException  {
        return null;
    }
}
