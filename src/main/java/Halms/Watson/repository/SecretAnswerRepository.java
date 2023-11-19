package Halms.Watson.repository;

import Halms.Watson.model.entity.SecretAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretAnswerRepository extends JpaRepository<SecretAnswer, Long> {
}
