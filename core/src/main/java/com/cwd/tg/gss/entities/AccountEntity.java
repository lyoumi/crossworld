package com.cwd.tg.gss.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class AccountEntity implements Serializable {
    @Id
    private String id;
    private String name;
    private String surname;
    private String login;
    private String password;

}
