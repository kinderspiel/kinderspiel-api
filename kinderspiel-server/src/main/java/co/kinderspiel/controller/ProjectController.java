package co.kinderspiel.controller;

import co.kinderspiel.converter.ProjectConverter;
import co.kinderspiel.dto.ProjectDto;
import co.kinderspiel.model.ProjectEntity;
import co.kinderspiel.repository.ProjectRepository;
import co.kinderspiel.until.HateoasHelper;
import io.rocketbase.commons.controller.BaseController;
import io.rocketbase.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController implements BaseController {

    private final ProjectRepository repository;

    private final ProjectConverter converter;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PagedModel<ProjectDto> find(@RequestParam(required = false) MultiValueMap<String, String> params) {
        Page<ProjectEntity> entities = repository.findAll(parsePageRequest(params, Sort.by("name")));
        List<ProjectDto> content = converter.fromEntities(entities.getContent());
        content.forEach(c -> {
            c.add(linkTo(methodOn(ProjectController.class).getById(c.getId())).withSelfRel());
        });
        return HateoasHelper.buildPageable(content, entities, params, ProjectController.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public ProjectDto getById(@PathVariable("id") String id) {
        ProjectEntity entity = getEntity(id);
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProjectDto create(@RequestBody @NotNull @Validated ProjectDto dto) {
        ProjectEntity entity = repository.save(converter.newEntity(dto));
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProjectDto update(@PathVariable String id, @RequestBody @NotNull @Validated ProjectDto dto) {
        ProjectEntity entity = getEntity(id);
        converter.updateEntityFromEdit(dto, entity);
        repository.save(entity);
        return addLinks(converter.fromEntity(entity));

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable("id") String id) {
        ProjectEntity entity = getEntity(id);
        repository.delete(entity);
    }

    protected ProjectEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    private ProjectDto addLinks(ProjectDto dto) {
        dto.add(linkTo(methodOn(ProjectController.class).getById(dto.getId())).withRel(IanaLinkRelations.EDIT));
        dto.add(linkTo(ColumnController.class, dto.getId()).withRel("columns"));
        dto.add(linkTo(CardController.class, dto.getId()).withRel("cards"));
        return dto;
    }

}
