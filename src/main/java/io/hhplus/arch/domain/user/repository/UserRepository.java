package io.hhplus.arch.domain.user.repository;

import io.hhplus.arch.domain.user.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User getById(Long id);
    User getOrCreateById(Long id);
    User save(User entity);
    void deleteAll();
}
