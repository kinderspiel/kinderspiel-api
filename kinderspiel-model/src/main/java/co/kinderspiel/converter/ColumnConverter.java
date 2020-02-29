package co.kinderspiel.converter;

import io.rocketbase.commons.converter.EntityReadWriteConverter;
import co.kinderspiel.dto.ColumnDto;
import co.kinderspiel.model.ColumnEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralConfig.class)
public interface ColumnConverter extends EntityReadWriteConverter<ColumnEntity, ColumnDto, ColumnDto> {

    ColumnDto fromEntity(ColumnEntity entity);

    @Mapping(target = "id", ignore = true)
    ColumnEntity newEntity(ColumnDto write);

    @InheritConfiguration()
    ColumnEntity updateEntityFromEdit(ColumnDto write, @MappingTarget ColumnEntity entity);
}
