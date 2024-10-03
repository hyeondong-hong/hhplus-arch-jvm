package io.hhplus.arch.infrastructure.user.repository;

import io.hhplus.arch.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
