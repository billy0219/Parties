package com.now.parties;

/**
 * Created by billy on 2017-08-25.
 */

class Users {
    private String email;

    private Users() {

    }

    public Users(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
