package co.kinderspiel.converter;

import io.rocketbase.commons.converter.EntityReadWriteConverter;
import co.kinderspiel.dto.TaskDto;
import co.kinderspiel.model.TaskEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralConfig.class)
public interface TaskConverter extends EntityReadWriteConverter<TaskEntity, TaskDto, TaskDto> {

    TaskDto fromEntity(TaskEntity entity);

    @Mapping(target = "id", ignore = true)
    TaskEntity newEntity(TaskDto write);

    @InheritConfiguration()
    TaskEntity updateEntityFromEdit(TaskDto write, @MappingTarget TaskEntity entity);
}
