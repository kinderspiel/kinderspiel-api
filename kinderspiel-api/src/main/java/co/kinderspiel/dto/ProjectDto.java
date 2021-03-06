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
public class ProjectDto extends RepresentationModel<ProjectDto> implements Serializable {

    private String id;

    private String name;

    private String description;

}
