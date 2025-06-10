package capsthon.backend.deeplung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import capsthon.backend.deeplung.domain.dto.request.JoinRequest;
import capsthon.backend.deeplung.domain.dto.request.LoginRequest;
import capsthon.backend.deeplung.domain.dto.response.UserInfoResponse;
import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.jwt.JwtAuthenticationResponse;
import capsthon.backend.deeplung.jwt.JwtTokenProvider;
import capsthon.backend.deeplung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	@Autowired
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public void createUser(JoinRequest joinRequest) {
		if (userRepository.findByUserId(joinRequest.getUserId()) == null) {
			User newUser = User.builder()
				.userId(joinRequest.getUserId())
				.password(passwordEncoder.encode(joinRequest.getPassword()))
				.name(joinRequest.getName())
				.gender(joinRequest.getGender())
				.isPrivateInformAgreed(joinRequest.getIsPrivateInformAgreed())
				.birthYear(joinRequest.getBirthYear())
				.build();
			userRepository.save(newUser);
		} else {
			throw new RuntimeException("이미 존재하는 회원입니다!");
		}
	}

	public JwtAuthenticationResponse login(LoginRequest loginRequest) {
		User user = userRepository.findByUserId(loginRequest.getUserId());
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new RuntimeException("password가 일치하지 않습니다.");
		}
		String token = jwtTokenProvider.generateToken(user);

		return new JwtAuthenticationResponse(token);
	}

	public UserInfoResponse info(UserDetails userDetails) {
		User user = userRepository.findByUserId(userDetails.getUsername());
		return UserInfoResponse.builder()
			.id(user.getId())
			.userType(user.getUserType())
			.userId(user.getUserId())
			.isPrivateInformAgreed(user.getIsPrivateInformAgreed())
			.birthYear(user.getBirthYear())
			.gender(user.getGender())
			.name(user.getName())
			.build();
	}

}
