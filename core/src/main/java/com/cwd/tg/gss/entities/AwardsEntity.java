package com.cwd.tg.gss.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class AwardsEntity implements Serializable {
    @Id
    private String id;
    private long experience;
    private long gold;
}
