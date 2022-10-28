package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public List<UserDTO> findAll(UserDTO condition) {
        List<User> users =  userRepository.findAll();
        return userMapper.toDTO(users);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        return userDTO;
    }
}
