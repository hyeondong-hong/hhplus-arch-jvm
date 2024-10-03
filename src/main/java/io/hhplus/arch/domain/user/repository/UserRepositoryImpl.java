package io.hhplus.arch.domain.user.repository;

import io.hhplus.arch.domain.user.entity.User;
import io.hhplus.arch.infrastructure.user.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(
                () -> new BadCredentialsException("유저가 없음: userId = " + id));
    }

    @Override
    @Transactional
    public User getOrCreateById(Long id) {
        return userJpaRepository.findById(id).orElseGet(
                () -> userJpaRepository.save(User.of(id)));
    }

    @Override
    public User save(User entity) {
        return userJpaRepository.save(entity);
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }
}
