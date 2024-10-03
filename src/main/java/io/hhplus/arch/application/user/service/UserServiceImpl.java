package io.hhplus.arch.application.user.service;

import io.hhplus.arch.domain.user.entity.User;
import io.hhplus.arch.domain.user.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User getOrCreateUser(
            @NonNull Long userId
    ) {
        return userRepository.getOrCreateById(userId);
    }
}
