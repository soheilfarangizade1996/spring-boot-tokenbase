package com.model.enu;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum RoleEnum implements GrantedAuthority {

    ROLE_ADMIN(1, "ادمین"),
    ROLE_USER(2,"کاربر");

    private Integer id;
    private String name;

    RoleEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleEnum getValue(int id){
        if (id == 1)
            return ROLE_ADMIN;
        else if (id == 2)
            return ROLE_USER;
        return RoleEnum.ROLE_USER;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return null;
    }
}
