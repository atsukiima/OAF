/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import edu.njit.cs.saboc.blu.core.abn.provenance.AbNDerivation;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance.SCTBasicStatedPAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.sno.abn.tan.provenance.SCTStatedTANDerivation;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTPropertyLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hl395
 */
public class SCTDerivationJSONParser extends AbNDerivationCoreParser<SCTRelease, SCTConceptLocationDataFactory, SCTPropertyLocationDataFactory, SCTAbNDerivationFactory>{
    
    
    private final SCTRelease sctRelease;
    private final SCTReleaseWithStated sctReleaseStated;
    private final Set<SCTConcept> sourceConcept;
    
    public SCTDerivationJSONParser(SCTRelease sctRelease, SCTConceptLocationDataFactory conceptFactory, SCTPropertyLocationDataFactory propertyFactory, SCTAbNDerivationFactory abnFactory){
        super(sctRelease, conceptFactory, propertyFactory, abnFactory);
        this.sctRelease = sctRelease;
        this.sourceConcept = null;
        this.sctReleaseStated =null;
        
    }
    
    public <T extends AbNDerivation> T coreParser(JSONArray jsonArr) throws Exception{
    
        System.out.println(jsonArr);
            boolean flag = false;
            
            JSONObject resultObject = super.findJSONObjectByName(jsonArr, "ClassName");
            String className = resultObject.get("ClassName").toString();
            JSONObject jsonObject = super.findJSONObjectByName(jsonArr, "BaseDerivation");
            JSONArray arr_base = (JSONArray) jsonObject.get("BaseDerivation");
            JSONObject innerjsonObject = super.findJSONObjectByName(arr_base, "BaseDerivation");
            if (!innerjsonObject.isEmpty()){
                System.out.println("Get inner derication and flag is on ");
                    flag = true;
            }
            
            SCTConceptLocationDataFactory conceptFactory = new SCTConceptLocationDataFactory(sourceConcept);
            SCTPropertyLocationDataFactory propertyFactory = new SCTPropertyLocationDataFactory(sourceConcept);
            SCTAbNDerivationFactory abnFactory = new SCTAbNDerivationFactory();
            
            if (className.equalsIgnoreCase("SCTBasicStatedPAreaTaxonomyDerivation")) {
                
                return (T) parseSCTBasicStatedPAreaTaxonomyDerivation();
            
            } else if (className.equalsIgnoreCase("SCTStatedTANDerivation")) {
                
                return  (T) parseSCTStatedTANDerivation();
            
            
            }else {
                if (flag) return super.coreParser(jsonArr, sctRelease, conceptFactory, propertyFactory, abnFactory, coreParser(arr_base));
                  
                return (T) super.coreParser(jsonArr, sctRelease, conceptFactory, propertyFactory, abnFactory);      
            }
            
    
    }
    
    // need to be implemented
    public SCTBasicStatedPAreaTaxonomyDerivation parseSCTBasicStatedPAreaTaxonomyDerivation(){
    
    
    
        return new SCTBasicStatedPAreaTaxonomyDerivation(null);
    }
    
        // need to be implemented
    public SCTStatedTANDerivation parseSCTStatedTANDerivation(){
    
        return  new SCTStatedTANDerivation(null);
    
    }
}
