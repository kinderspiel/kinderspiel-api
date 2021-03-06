package co.kinderspiel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "project")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;
}
