package com.example.demo.service;

import java.util.List;

public interface IService<ENTITY, DTO> {
    List<DTO> findAll(DTO condition);

    DTO save(DTO dto);
}
