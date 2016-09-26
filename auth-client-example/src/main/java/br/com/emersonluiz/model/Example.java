package br.com.emersonluiz.model;

import java.io.Serializable;

public class Example implements Serializable {

    private static final long serialVersionUID = -8835388690693616927L;

    private int id;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
