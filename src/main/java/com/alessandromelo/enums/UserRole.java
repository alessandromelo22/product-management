package com.alessandromelo.enums;

/*
Hierarquia das roles, da maior para a menor
 */
public enum UserRole {

    OWNER("Owner", 3),
    ADMIN("Administrator", 2),
    USER("User", 1);

    private final String description;
    private final Integer permisionLevel;

    UserRole(String description, Integer permisionLevel) {
        this.description = description;
        this.permisionLevel = permisionLevel;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPermisionLevel() {
        return permisionLevel;
    }


}
