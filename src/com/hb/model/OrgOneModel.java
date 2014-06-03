package com.hb.model;

import java.util.List;


public class OrgOneModel {

    /**
     * province name
     */
    private String name;

    /**
     * province code
     */
    private int code;

    /**
     * province have orgTwoModels
     */
    private List<OrgTwoModel> orgTwoModels;

    public OrgOneModel() {
        super();
    }
    
    public OrgOneModel(String name, int code) {
        super();
        this.name = name;
        this.code = code;
    }

    public OrgOneModel(String name, int code, List<OrgTwoModel> orgTwoModels) {
        super();
        this.name = name;
        this.code = code;
        this.orgTwoModels = orgTwoModels;
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
     * Getter of orgTwoModels
     * 
     * @return the orgTwoModels
     */
    public List<OrgTwoModel> getTwos() {
        return orgTwoModels;
    }

    /**
     * Setter of orgTwoModels
     * 
     * @param orgTwoModels
     *            the orgTwoModels to set
     */
    public void setOrgTwoModels(List<OrgTwoModel> orgTwoModels) {
        this.orgTwoModels = orgTwoModels;
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