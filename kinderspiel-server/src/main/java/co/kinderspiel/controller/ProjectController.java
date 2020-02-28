package co.kinderspiel.controller;

import io.rocketbase.commons.dto.PageableResult;
import io.rocketbase.commons.exception.NotFoundException;
import io.rocketbase.commons.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import co.kinderspiel.converter.ProjectConverter;
import co.kinderspiel.dto.ProjectDto;
import co.kinderspiel.model.ProjectEntity;
import co.kinderspiel.repository.ProjectRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;

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
    public PageableResult<ProjectDto> find(@RequestParam(required = false) MultiValueMap<String, String> params) {
        Page<ProjectEntity> entities = repository.findAll(parsePageRequest(params, getDefaultSort()));
        return PageableResult.contentPage(converter.fromEntities(entities.getContent()), entities);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public ProjectDto getById(@PathVariable("id") String id) {
        ProjectEntity entity = getEntity(id);
        return converter.fromEntity(entity);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProjectDto create(@RequestBody @NotNull @Validated ProjectDto dto) {
        ProjectEntity entity = repository.save(converter.newEntity(dto));
        return converter.fromEntity(entity);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProjectDto update(@PathVariable String id, @RequestBody @NotNull @Validated ProjectDto dto) {
        ProjectEntity entity = getEntity(id);
        converter.updateEntityFromEdit(dto, entity);
        repository.save(entity);
        return converter.fromEntity(entity);
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

    protected Sort getDefaultSort() {
        return Sort.by("id");
    }

}
