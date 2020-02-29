package co.kinderspiel.converter;

import io.rocketbase.commons.converter.EntityReadWriteConverter;
import co.kinderspiel.dto.CardDto;
import co.kinderspiel.model.CardEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralConfig.class)
public interface CardConverter extends EntityReadWriteConverter<CardEntity, CardDto, CardDto> {

    CardDto fromEntity(CardEntity entity);

    @Mapping(target = "id", ignore = true)
    CardEntity newEntity(CardDto write);

    @InheritConfiguration()
    CardEntity updateEntityFromEdit(CardDto write, @MappingTarget CardEntity entity);
}
