package istic.m2.ila.firefighterapp.addintervention;

/**
 * Created by amendes on 26/03/18.
 */

public class CodeSinistreDTO {
    private Long code;
    private String description;

    public CodeSinistreDTO(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString(){
        return this.code+" - "+this.description;
    }
}
