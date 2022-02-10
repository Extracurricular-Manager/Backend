package fr.periscol.backend.web.rest.vm;

import javax.validation.constraints.Size;

/**
 * View Model object for storing the user's key and password.
 */
public class PasswordVM {

    @Size(min = 4, max = 100)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordVM(String password) {
        this.password = password;
    }

    public PasswordVM() {}
}
