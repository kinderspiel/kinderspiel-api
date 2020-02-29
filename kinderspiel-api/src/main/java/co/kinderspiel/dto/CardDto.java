package co.kinderspiel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto extends RepresentationModel<CardDto> implements Serializable {

    private String id;

    private String projectId;

    private String columnId;

    private String name;

    private String description;

    private Integer priority;
}
