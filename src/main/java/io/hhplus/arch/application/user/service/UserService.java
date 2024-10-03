package io.hhplus.arch.application.user.service;

import io.hhplus.arch.domain.user.entity.User;

public interface UserService {
    User getUser(Long userId);
    User getOrCreateUser(Long userId);
}
