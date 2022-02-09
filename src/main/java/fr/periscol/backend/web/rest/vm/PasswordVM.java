package fr.periscol.backend.web.rest.vm;

/**
 * View Model object for storing the user's key and password.
 */
public class PasswordVM {

    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public PasswordVM(String newPassword) {
        this.newPassword = newPassword;
    }

    public PasswordVM() {}
}
