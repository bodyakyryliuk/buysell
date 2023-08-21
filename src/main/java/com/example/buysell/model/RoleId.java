package com.example.buysell.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleId implements Serializable {

    private String email;
    private String role;
}