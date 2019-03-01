package com.crossworld.web.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class AccountEntity implements Serializable {
    private String id;
    private String name;
    private String surname;
    private String login;
    private String password;

}
