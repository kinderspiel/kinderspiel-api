package co.kinderspiel.repository;

import co.kinderspiel.model.ProjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {

}
