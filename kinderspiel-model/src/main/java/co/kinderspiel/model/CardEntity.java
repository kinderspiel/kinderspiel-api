package co.kinderspiel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document(collection = "card")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity implements Serializable {

    @Id
    private String id;

    @Indexed
    @NotNull
    private String projectId;

    private String name;

    private String description;

    private Integer priority;
}
