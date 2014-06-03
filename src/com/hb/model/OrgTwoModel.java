package com.hb.model;

public class OrgTwoModel {

    /**
     * city name
     */
    private String name;

    /**
     * province code;
     */
    private int orgtwo_code;

    /**
     * city code
     */
    private int code;

    
    public OrgTwoModel() {
        super();
    }
    
    public OrgTwoModel(String name, int orgtwo_code, int code) {
        super();
        this.name = name;
        this.orgtwo_code = orgtwo_code;
        this.code = code;
    }

    /**
     * Getter of name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of name
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of orgtwo_code
     * 
     * @return the orgtwo_code
     */
    public int getProvince_code() {
        return orgtwo_code;
    }

    /**
     * Setter of orgtwo_code
     * 
     * @param orgtwo_code
     *            the orgtwo_code to set
     */
    public void setProvince_code(int orgtwo_code) {
        this.orgtwo_code = orgtwo_code;
    }

    /**
     * Getter of code
     * 
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Setter of code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}