package com.zemrow.module.integration.freshdesk.dsl.dto;

/**
 * Пользователь
 *
 * @author Alexandr Polyakov on 2022.01.12
 */
public class UserDto {
    private String email;
    private String fullName;

    public UserDto(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
