package fr.periscol.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;

public class NewUserDTO {

    private String name;

    @Size(min = 1, max = 50)
    private String login;

    @Size(min = 4, max = 100)
    @JsonProperty("password")
    private String defaultPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
