package io.hhplus.arch.integration.common.transaction;

import io.hhplus.arch.common.transaction.FunctionalTransaction;
import io.hhplus.arch.common.transaction.TransactionItem;
import io.hhplus.arch.domain.lecture.entity.Lecture;
import io.hhplus.arch.domain.lecture.repository.LectureRepository;
import io.hhplus.arch.domain.user.entity.User;
import io.hhplus.arch.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionDefinition;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FunctionalTransactionIntegrationTest {

    @Autowired
    private FunctionalTransaction functionalTransaction;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        lectureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("다중 트랜잭션 체인 롤백 테스트")
    void rollback_transactionsRollbackTogether() {
        assertThrows(RuntimeException.class, () -> {
            functionalTransaction.perform(new TransactionItem<>(handler1 -> {
                Lecture lecture = new Lecture();
                lecture.setTitle("Test Lecture");
                lecture.setDefaultCapacity(10);
                lecture.setLecturerName("Lecturer");
                lectureRepository.save(lecture);

                return new TransactionItem<>(handler2 -> {
                    User user = User.of(10L);
                    userRepository.save(user);

                    throw new RuntimeException("Second Transaction Failure");
                });
            }));
        });

        assertTrue(lectureRepository.findAll().isEmpty(), "첫 번째 트랜잭션 블록이 두 번째 트랜잭션 블록과 함께 롤백되어야 함.");
        assertTrue(userRepository.findAll().isEmpty(), "두 번째 트랜잭션 블록이 롤백되어야 함.");
    }

    @Test
    @DisplayName("Mandatory 전파수준 테스트")
    void propagation_transactionsMandatory() {
        assertThrows(IllegalTransactionStateException.class, () -> {
            functionalTransaction.perform(new TransactionItem<>(
                        false,
                        TransactionDefinition.ISOLATION_DEFAULT,
                        TransactionDefinition.PROPAGATION_MANDATORY,
                        handler -> {
                Lecture lecture = new Lecture();
                lecture.setTitle("Test Lecture");
                lecture.setDefaultCapacity(10);
                lecture.setLecturerName("Lecturer");
                return lectureRepository.save(lecture);
            }));
        });

        assertTrue(lectureRepository.findAll().isEmpty(), "외부 트랜잭션이 없을 경우 예외가 발생하기 때문에 저장되지 않아야 함.");
    }

    @Test
    @DisplayName("Never 전파수준 테스트")
    void propagation_transactionsNever() {
        assertThrows(IllegalTransactionStateException.class, () -> {
            functionalTransaction.perform(new TransactionItem<>(handler1 -> {
                Lecture lecture = new Lecture();
                lecture.setTitle("Test Lecture");
                lecture.setDefaultCapacity(10);
                lecture.setLecturerName("Lecturer");
                lectureRepository.save(lecture);

                return new TransactionItem<>(
                        false,
                        TransactionDefinition.ISOLATION_DEFAULT,
                        TransactionDefinition.PROPAGATION_NEVER,
                        handler2 -> {
                    User user = User.of(10L);
                    return userRepository.save(user);
                });
            }));
        });

        assertTrue(lectureRepository.findAll().isEmpty(), "두 번째 블록에서 예외가 발생했기 때문에 롤백되어야 함.");
        assertTrue(userRepository.findAll().isEmpty(), "외부 트랜잭션이 존재할 경우 예외가 발생하기 때문에 저장되지 않아야 함.");
    }
}
