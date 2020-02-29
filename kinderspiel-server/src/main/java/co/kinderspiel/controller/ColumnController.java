package co.kinderspiel.controller;

import co.kinderspiel.converter.ColumnConverter;
import co.kinderspiel.dto.ColumnDto;
import co.kinderspiel.model.ColumnEntity;
import co.kinderspiel.model.ProjectEntity;
import co.kinderspiel.repository.ColumnRepository;
import co.kinderspiel.repository.ProjectRepository;
import co.kinderspiel.until.HateoasHelper;
import io.rocketbase.commons.controller.BaseController;
import io.rocketbase.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/project/{projectId}/column")
@RequiredArgsConstructor
public class ColumnController implements BaseController {

    private final ColumnRepository columnRepository;

    private final ProjectRepository projectRepository;

    private final ColumnConverter converter;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PagedModel<ColumnDto> find(@PathVariable("projectId") String projectId, @RequestParam(required = false) MultiValueMap<String, String> params) {
        Page<ColumnEntity> entities = findAllByProjectId(projectId, parsePageRequest(params, getDefaultSort()));
        List<ColumnDto> content = converter.fromEntities(entities.getContent());
        content.forEach(c -> {
            c.add(linkTo(methodOn(ColumnController.class).getEntity(c.getProjectId(), c.getId())).withSelfRel());
        });
        return HateoasHelper.buildPageable(content, entities, params, ColumnController.class, projectId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public ColumnDto getById(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        ColumnEntity entity = getEntity(projectId, id);
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ColumnDto create(@PathVariable("projectId") String projectId, @RequestBody @NotNull @Validated ColumnDto dto) {
        ColumnEntity entity = columnRepository.save(newEntity(projectId, dto));
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ColumnDto update(@PathVariable("projectId") String projectId, @PathVariable String id, @RequestBody @NotNull @Validated ColumnDto dto) {
        ColumnEntity entity = getEntity(projectId, id);
        converter.updateEntityFromEdit(dto, entity);
        columnRepository.save(entity);
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        ColumnEntity entity = getEntity(projectId, id);
        columnRepository.delete(entity);
    }

    protected ColumnEntity getEntity(String projectId, String id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    protected Sort getDefaultSort() {
        return Sort.by("id");
    }

    protected Page<ColumnEntity> findAllByProjectId(String projectId, Pageable pageable) {
        return columnRepository.findByProjectId(projectId, pageable);
    }

    protected ColumnEntity newEntity(String projectId, ColumnDto dto) {
        ProjectEntity parent = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException());

        ColumnEntity entity = converter.newEntity(dto);
        entity.setProjectId(projectId);
        return entity;
    }

    private ColumnDto addLinks(ColumnDto dto) {
        dto.add(linkTo(methodOn(ColumnController.class, dto.getProjectId()).getById(dto.getProjectId(), dto.getId())).withRel(IanaLinkRelations.EDIT));
        return dto;
    }
}
