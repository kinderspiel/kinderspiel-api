package co.kinderspiel.repository;

import co.kinderspiel.model.ColumnEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColumnRepository extends MongoRepository<ColumnEntity, String> {

    Page<ColumnEntity> findByProjectId(String projectId, Pageable pageable);

}
