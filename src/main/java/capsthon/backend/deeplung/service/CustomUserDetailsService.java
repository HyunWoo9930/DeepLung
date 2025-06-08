package capsthon.backend.deeplung.service;

import org.springframework.stereotype.Service;

import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

	private final UserRepository userRepository;

	public User findByUserId(String userName) {
		return userRepository.findByUserId(userName);
	}
}
