package gov.uscis.vis.api.models;

/**
 * Created by cedennis on 1/30/17.
 */
public enum StateEnum {
    ACTIVE("active"), CLOSED("closed"), FUTURE("future");
    StateEnum(String label){
        this.label = label;
    }
    private String label;
    public String getLabel(){
        return label;
    }
}
