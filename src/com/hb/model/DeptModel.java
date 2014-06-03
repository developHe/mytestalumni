package com.hb.model;

import java.util.List;


public class DeptModel {

    /**
     * province name
     */
    private String name;

    /**
     * province code
     */
    private int code;

    /**
     * province have majors
     */
    private List<MajorModel> majors;

    public DeptModel() {
        super();
    }
    
    public DeptModel(String name, int code) {
        super();
        this.name = name;
        this.code = code;
    }

    public DeptModel(String name, int code, List<MajorModel> majors) {
        super();
        this.name = name;
        this.code = code;
        this.majors = majors;
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
     * Getter of majors
     * 
     * @return the majors
     */
    public List<MajorModel> getMajors() {
        return majors;
    }

    /**
     * Setter of majors
     * 
     * @param majors
     *            the majors to set
     */
    public void setMajors(List<MajorModel> majors) {
        this.majors = majors;
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