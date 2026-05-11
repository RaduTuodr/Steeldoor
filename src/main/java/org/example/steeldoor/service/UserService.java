package org.example.steeldoor.service;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.model.User;
import org.example.steeldoor.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(int id) { return userRepository.findById(id).orElse(null); }
}
