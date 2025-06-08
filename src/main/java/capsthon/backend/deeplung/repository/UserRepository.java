package capsthon.backend.deeplung.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import capsthon.backend.deeplung.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserId(String userId);
}
