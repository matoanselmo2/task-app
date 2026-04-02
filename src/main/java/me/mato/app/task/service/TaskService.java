package me.mato.app.task.service;

import me.mato.app.exception.TaskNotFoundException;
import me.mato.app.exception.UnauthorizedException;
import me.mato.app.task.model.Task;
import me.mato.app.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

	private final TaskRepository repository;

	public TaskService(TaskRepository repository) {
		this.repository = repository;
	}

	public boolean isTitleTakenByOwner(String title, ObjectId ownerId) {
		return repository.findByTitleAndOwnerId(title, ownerId).isPresent();
	}

	public void createTask(String title, ObjectId ownerId) {
		Task task = new Task();
		task.setTitle(title);
		task.setOwnerId(ownerId);
		task.setCreatedAt(LocalDateTime.now());
		repository.save(task);
	}

	public Task getTaskById(ObjectId id, ObjectId ownerId) {
		Task task = repository.findById(id).orElse(null);

		if (task != null && (task.getOwnerId() == null || !task.getOwnerId().equals(ownerId))) {
			throw new UnauthorizedException();
		}

		if (task == null) {
			throw new TaskNotFoundException();
		}

		return task;
	}

	public Task renameTask(ObjectId id, String newTitle, ObjectId ownerId) {
		Task task = getTaskById(id, ownerId);

		task.setTitle(newTitle);
		return repository.save(task);
	}

	public Task markTaskAsCompleted(ObjectId id, ObjectId ownerId) {
		Task task = getTaskById(id, ownerId);

		if (task.getOwnerId() == null || !task.getOwnerId().equals(ownerId)) {
			throw new UnauthorizedException();
		}

		task.setCompleted(true);
		return repository.save(task);
	}

	public void deleteTask(ObjectId id, ObjectId ownerId) {
		if (!repository.existsById(id)) {
			throw new TaskNotFoundException();
		}

		if (!repository.existsByIdAndOwnerId(id, ownerId)) {
			throw new UnauthorizedException();
		}

		repository.deleteById(id);
	}

	public List<Task> getTasksByOwnerId(ObjectId ownerId) {
		return repository.findByOwnerId(ownerId);
	}
}
