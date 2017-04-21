package edu.njit.cs.saboc.blu.sno.abn.tan.provenance;

import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;
import org.json.simple.JSONObject;

/**
 * Stores the arguments needed to create a TAN from the stated
 * hierarchical relationships of a hierarchy of concepts
 * 
 * @author Chris O
 */
public class SCTStatedTANDerivation extends SimpleClusterTANDerivation {
    
    private final SimpleClusterTANDerivation base;
    
    public SCTStatedTANDerivation(SimpleClusterTANDerivation base) {
        super(base);
        
        this.base = base;
    }

    @Override
    public String getDescription() {
        return "Derived Stated Cluster TAN";
    }
    
    @Override
    public JSONObject serializeToJSON() {
        JSONObject result = new JSONObject();
        
        result.put("ClassName", "SCTStatedTANDerivation");       
        result.put("BaseDerivation", base.serializeToJSON());   
        
        return result;
    }    
}
