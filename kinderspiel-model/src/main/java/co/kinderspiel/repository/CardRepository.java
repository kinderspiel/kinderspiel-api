package co.kinderspiel.repository;

import co.kinderspiel.model.CardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<CardEntity, String> {

    Page<CardEntity> findByProjectId(String projectId, Pageable pageable);
}
