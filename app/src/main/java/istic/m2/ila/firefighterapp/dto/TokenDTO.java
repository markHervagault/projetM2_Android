package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/27/18.
 */

public class TokenDTO {

    private String id_token;

    public TokenDTO(String token){
        this.id_token = token;
    }
    public TokenDTO(){

    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

}
