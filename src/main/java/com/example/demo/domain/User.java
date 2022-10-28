package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "users")
@Data
public class User extends AbstractAuditingEntity {
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 50)
    @Indexed
    private String login;
}
