package com.arins.techstore.models;


import lombok.*;

@Getter
@Setter
@ToString
public class SignUpRequest {

    private String username;
    private String email;
    private String password;

}