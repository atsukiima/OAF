package edu.njit.cs.saboc.blu.sno.properties;

/**
 * Singleton class that manages all of the properties of the SNOMED CT
 * Graphing Tool program.
 * @author Chris
 */
public class AreaToolProperties {

    /**
     * The singleton instance of the AreaTool Properties
     */
    private static AreaToolProperties toolPropertiesRef;

    /**
     * Creates or returns the singleton instance of AreaToolProperties
     * @return The instance of AreaToolProperties
     */
    public static AreaToolProperties getAreaToolProperties() {
        if(toolPropertiesRef == null) {
            toolPropertiesRef = new AreaToolProperties();
        }

        return toolPropertiesRef;
    }

    /***
     * The web address where the middleware servlet resides
     */
    private String hostAddress = "http://nat.njit.edu/";

    /***
     * The path on the webserver where the middleware servlet is running. e.g. nat.njit.edu/<b>NATServlet/</b>
     */
    private String servletLocation = "NATServlet/";

    /***
     * Creates a new AreaToolProperties object.
     */
    private AreaToolProperties() {
        
    }

    /***
     * Sets the web address where the middleware servlet resides
     * @param host The web address where the middleware servlet resides
     */
    public void setHostAddress(String host) {
        this.hostAddress = host;
    }

    /***
     * Returns the web address where the middleware servlet resides
     * @return The host name of where the middlewhere servlet is running
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /***
     * Sets the path where the middleware servlet is executing on the web server.
     * @param location The path where the servlet is located on the middleware web server.
     */
    public void setServletLocation(String location) {
        this.servletLocation = location;
    }

    /***
     * Returns the path where the 
     * @return String which contains the path and a forward slash at the end.
     */
    public String getServletLocation() {
        return servletLocation;
    }
}
