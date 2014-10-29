package edu.njit.cs.saboc.blu.sno.ddirules;

/**
 *
 * @author Aleks
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDIDataLoader {

    private static Pattern effectdecreasingpattern = Pattern.compile("(decrease)|(reduce)|(antagonize)|(antagonism)", Pattern.CASE_INSENSITIVE);//We need to check if it was active or passive to ensure proper application of properties
    private static Pattern effectincreasingpattern = Pattern.compile("increase", Pattern.CASE_INSENSITIVE);
    private static Pattern unclassifiedeffectchangepattern = Pattern.compile("contribute", Pattern.CASE_INSENSITIVE);
    private static Pattern modifierpattern = Pattern.compile("(be decreased)|(decrease)|(increase)|(be increased)|(alter(s)?\\b)|(be altered)|(reduce)|(enhance)", Pattern.CASE_INSENSITIVE);
    private static Pattern probabilitypattern = Pattern.compile("may(( occur)|( cause)|( result)|( antagonize)|( augment)|( contribute)|( impair))", Pattern.CASE_INSENSITIVE);
    private static HashMap<String, RuleObject> entrymap = new HashMap<String, RuleObject>();

    /**
     * @param filename - name of the csv file to be loaded
     *
     *
     */
    private static void loadFile(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            boolean captureallowed = false;
            int start_pos = 0;
            ArrayList<String> cleaninit = new ArrayList<String>();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '"') {
                    if (!captureallowed) {
                        start_pos = i + 1;
                    } else {
                        cleaninit.add(line.substring(start_pos, i));
                        i++;
                        start_pos = i + 1;
                    }
                    captureallowed = !captureallowed;
                } else if ((!captureallowed) && (line.charAt(i) == ';')) {
                    cleaninit.add(line.substring(start_pos, i));
                    start_pos = i + 1;
                }
            }
            cleaninit.add(line.substring(start_pos, line.length()));
            //Basic parsing done.
            if (cleaninit.size() != 4) {
                System.out.println("Error");
            } else {
                Matcher m = modifierpattern.matcher(cleaninit.get(2));
                if (!m.find()) {
                    m = probabilitypattern.matcher(cleaninit.get(2));
                    if (!m.find()) {
                        //System.out.println(cleaninit.get(2));
                    }
                } else {
                    String key = cleaninit.get(3).toLowerCase().trim();
                    if (entrymap.containsKey(key)) {
                        entrymap.get(key).addRule(cleaninit.get(1), cleaninit.get(2));
                    } else {
                        entrymap.put(key, new RuleObject(key, cleaninit.get(1), cleaninit.get(2)));
                    }
                }
            }
            //System.exit(0);
        }
        br.close();

    }

    public static void loadRuleSet(String path) {
        try {
            loadFile(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getRule(String concept) {
        String localconcept = concept;
        int location = localconcept.lastIndexOf("(");
        if (location != -1) {
            localconcept = localconcept.substring(0, location).trim().toLowerCase();
        } else {
            localconcept = localconcept.trim().toLowerCase();
        }
        if (entrymap.get(localconcept) != null) {
            return entrymap.get(localconcept).toString();//Once again, with custom objects it should be rewritten
        }
        return null;
    }

    public static RuleObject getRuleObject(String concept) {
        String localconcept = concept;
        int location = localconcept.lastIndexOf("(");
        if (location != -1) {
            localconcept = localconcept.substring(0, location).trim().toLowerCase();
        } else {
            localconcept = localconcept.trim().toLowerCase();
        }
        RuleObject localobject = entrymap.get(localconcept);
        if (localobject == null) {
            return new RuleObject();//Once again, with custom objects it should be rewritten
        }
        return localobject;
    }

}
