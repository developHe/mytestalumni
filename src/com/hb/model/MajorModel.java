package com.hb.model;

public class MajorModel {

    /**
     * city name
     */
    private String name;

    /**
     * province code;
     */
    private int major_code;

    /**
     * city code
     */
    private int code;

    
    public MajorModel() {
        super();
    }
    
    public MajorModel(String name, int major_code, int code) {
        super();
        this.name = name;
        this.major_code = major_code;
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
     * Getter of major_code
     * 
     * @return the major_code
     */
    public int getProvince_code() {
        return major_code;
    }

    /**
     * Setter of major_code
     * 
     * @param major_code
     *            the major_code to set
     */
    public void setProvince_code(int major_code) {
        this.major_code = major_code;
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