package me.mato.app.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.mato.app.user.model.User;
import me.mato.app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping("/createUser")
	public ResponseEntity<String> createUser(@RequestBody AuthRequest request) {
		if (service.isUsernameTaken(request.name())) {
			return ResponseEntity.status(409).body("Username is already taken");
		}

		service.createUser(request.name(), request.password());
		return ResponseEntity.status(201).body("User created successfully!");
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
		User user = service.loginUser(request.name(), request.password());

		HttpSession session = httpRequest.getSession(true);
		session.setAttribute("userId", user.getId().toString());

		return ResponseEntity.ok("Login successful");
	}

	public record AuthRequest(String name, String password) { }
}
