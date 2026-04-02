package me.mato.app.task.repository;

import me.mato.app.task.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {
	Optional<Task> findByTitleAndOwnerId(String title, ObjectId ownerId);
	List<Task> findByOwnerId(ObjectId ownerId);
	boolean existsByIdAndOwnerId(ObjectId id, ObjectId ownerId);
}
