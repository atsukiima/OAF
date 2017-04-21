package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;
import org.json.simple.JSONObject;

/**
 * Identifies the arguments needed to create a stated partial-area taxonomy from
 * a hierarchy of concepts
 *
 * @author Chris O
 */
public class SCTBasicStatedPAreaTaxonomyDerivation extends SimplePAreaTaxonomyDerivation {

    private final SimplePAreaTaxonomyDerivation base;

    public SCTBasicStatedPAreaTaxonomyDerivation(
            SimplePAreaTaxonomyDerivation baseDerivation) {

        super(baseDerivation);

        this.base = baseDerivation;
    }

    @Override
    public String getDescription() {
        return "Derived SCT Stated PArea Taxonomy";
    }

    @Override
    public String getName() {
        return String.format("%s (stated)", super.getName());
    }
    
    @Override
    public JSONObject serializeToJSON() {
        JSONObject result = new JSONObject();

        result.put("ClassName", "SCTBasicStatedPAreaTaxonomyDerivation");
        result.put("BaseDerivation", base.serializeToJSON());

        return result;

    }
}
