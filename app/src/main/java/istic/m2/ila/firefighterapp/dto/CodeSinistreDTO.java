package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;

/**
 * Created by hakima on 3/27/18.
 */

public class CodeSinistreDTO implements Serializable {

    private Long id;

    private String code;

    private String intitule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
}
