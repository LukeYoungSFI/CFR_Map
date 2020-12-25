package com.sfi.cfrmap.bean;

public class GeneralLookupBean {

	String value;
    String option;

    public GeneralLookupBean () {
        
    }
    public GeneralLookupBean (String value,String option) {
        this.value=value;
        this.option=option;
        
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getOption() {
        return option;
    }
    public void setOption(String option) {
        this.option = option;
    }
}
