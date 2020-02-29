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
public class ColumnDto extends RepresentationModel<ColumnDto> implements Serializable {

    private String id;

    private String projectId;

    private String name;

    private String color;

    private int order;
}
