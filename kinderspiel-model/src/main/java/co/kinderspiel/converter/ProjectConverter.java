package co.kinderspiel.converter;

import io.rocketbase.commons.converter.EntityReadWriteConverter;
import co.kinderspiel.dto.ProjectDto;
import co.kinderspiel.model.ProjectEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralConfig.class)
public interface ProjectConverter extends EntityReadWriteConverter<ProjectEntity, ProjectDto, ProjectDto> {

    ProjectDto fromEntity(ProjectEntity entity);

    @Mapping(target = "id", ignore = true)
    ProjectEntity newEntity(ProjectDto write);

    @InheritConfiguration()
    ProjectEntity updateEntityFromEdit(ProjectDto write, @MappingTarget ProjectEntity entity);
}
