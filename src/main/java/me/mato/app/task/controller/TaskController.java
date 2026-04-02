package me.mato.app.task.controller;

import jakarta.servlet.http.HttpSession;
import me.mato.app.task.service.TaskService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@GetMapping("/debug")
	public ResponseEntity<?> debug(HttpSession session) {
		return ResponseEntity.ok(session.getAttribute("userId"));
	}

	@PostMapping
	public ResponseEntity<String> createTask(@RequestBody TaskRequest request, HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		if (service.isTitleTakenByOwner(request.title(), userId)) {
			return ResponseEntity.status(409).body("Task title is already taken for this user");
		}

		service.createTask(request.title(), userId);
		return ResponseEntity.status(201).body("Task created successfully!");
	}

	@GetMapping
	public ResponseEntity<?> getTasksByOwnerId(HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		return ResponseEntity.ok(service.getTasksByOwnerId(userId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTaskById(@PathVariable String id, HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		return ResponseEntity.ok(service.getTaskById(new ObjectId(id), userId));
	}

	@PatchMapping("/{id}/rename")
	public ResponseEntity<?> renameTask(@PathVariable String id, @RequestBody TaskRequest request, HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		return ResponseEntity.ok(service.renameTask(new ObjectId(id), request.title(), userId));
	}

	@PatchMapping("/{id}/complete")
	public ResponseEntity<?> markTaskAsCompleted(@PathVariable String id, HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		return ResponseEntity.ok(service.markTaskAsCompleted(new ObjectId(id), userId));
	}

	@DeleteMapping("/{id}/delete")
	public ResponseEntity<?> deleteTask(@PathVariable String id, HttpSession session) {
		ObjectId userId = new ObjectId((String) session.getAttribute("userId"));

		service.deleteTask(new ObjectId(id), userId);
		return ResponseEntity.ok("Task deleted successfully!");
	}

	public record TaskRequest(String title) { }
}
