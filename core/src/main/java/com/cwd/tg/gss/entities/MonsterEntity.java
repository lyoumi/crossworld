package com.cwd.tg.gss.entities;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class MonsterEntity implements Serializable {
    @Id
    private String id;
    private long hitPoints;
    private long attack;
    private String monsterType;

    @CreatedDate
    private DateTime createdAt;
    @LastModifiedDate
    private DateTime lastModifiedAt;
}
