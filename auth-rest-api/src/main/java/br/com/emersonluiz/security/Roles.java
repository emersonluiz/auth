package br.com.emersonluiz.security;

public enum Roles {

    CLIENT_CREATE_EXAMPLE("CLIENT:ROLE_CREATE_EXAMPLE");

    private String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static boolean contains(String s) {
        for(Roles role:values()) {
            if (role.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
