package capsthon.backend.deeplung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import capsthon.backend.deeplung.domain.dto.response.PaperweightResponse;
import capsthon.backend.deeplung.domain.entity.Paperweight;
import capsthon.backend.deeplung.domain.entity.User;

public interface PaperweightRepository extends JpaRepository<Paperweight, Long> {
	List<Paperweight> findAllByUserOrderByCreatedAtDesc(User user);

	Paperweight findByIdAndUser(Long id, User user);

	long countByUser(User user);
}
