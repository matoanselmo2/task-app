package me.mato.app.user.service;

import me.mato.app.exception.InvalidCredentialsException;
import me.mato.app.user.model.User;
import me.mato.app.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	private final UserRepository repository;
	private final PasswordEncoder encoder;

	public UserService(UserRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	public boolean isUsernameTaken(String name) {
		return repository.findByName(name).isPresent();
	}

	public User loginUser(String name, String rawPassword) {
		Optional<User> optionalUser = repository.findByName(name);
		User user = optionalUser.orElse(null);

		if (user == null) {
			throw new InvalidCredentialsException();
		}

		if (!encoder.matches(rawPassword, user.getHashedPassword())) {
			throw new InvalidCredentialsException();
		}

		return user;
	}

	public void createUser(String name, String rawPassword) {
		User user = new User();
		user.setName(name);

		String hashedPassword = encoder.encode(rawPassword);
		user.setHashedPassword(hashedPassword);

		repository.save(user);
	}

}
