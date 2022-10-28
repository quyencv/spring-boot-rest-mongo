package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDTO extends AbstractAuditingDTO {

    private String id;

    private String login;
}
