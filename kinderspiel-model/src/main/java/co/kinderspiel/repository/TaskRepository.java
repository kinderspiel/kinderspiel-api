package co.kinderspiel.repository;

import co.kinderspiel.model.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {

}
