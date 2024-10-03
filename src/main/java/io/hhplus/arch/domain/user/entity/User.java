package io.hhplus.arch.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @PrePersist
    public void prePersist() {
        if (name == null) {
            name = "수강생 " + id;
        }
    }

    public static User of(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
