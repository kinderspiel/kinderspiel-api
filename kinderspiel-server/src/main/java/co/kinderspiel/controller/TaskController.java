package co.kinderspiel.controller;

import io.rocketbase.commons.dto.PageableResult;
import io.rocketbase.commons.exception.NotFoundException;
import io.rocketbase.commons.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import co.kinderspiel.converter.TaskConverter;
import co.kinderspiel.dto.TaskDto;
import co.kinderspiel.model.TaskEntity;
import co.kinderspiel.model.ProjectEntity;
import co.kinderspiel.repository.TaskRepository;
import co.kinderspiel.repository.ProjectRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/project/{projectId}/task")
@RequiredArgsConstructor
public class TaskController implements BaseController {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final TaskConverter converter;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PageableResult<TaskDto> find(@PathVariable("projectId") String projectId, @RequestParam(required = false) MultiValueMap<String, String> params) {
        Page<TaskEntity> entities = findAllByProjectId(projectId, parsePageRequest(params, getDefaultSort()));
        return PageableResult.contentPage(converter.fromEntities(entities.getContent()), entities);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public TaskDto getById(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        TaskEntity entity = getEntity(projectId, id);
        return converter.fromEntity(entity);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskDto create(@PathVariable("projectId") String projectId, @RequestBody @NotNull @Validated TaskDto dto) {
        TaskEntity entity = taskRepository.save(newEntity(projectId, dto));
        return converter.fromEntity(entity);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskDto update(@PathVariable("projectId") String projectId, @PathVariable String id, @RequestBody @NotNull @Validated TaskDto dto) {
        TaskEntity entity = getEntity(projectId, id);
        converter.updateEntityFromEdit(dto, entity);
        taskRepository.save(entity);
        return converter.fromEntity(entity);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        TaskEntity entity = getEntity(projectId, id);
        taskRepository.delete(entity);
    }

    protected TaskEntity getEntity(String projectId, String id) {
        // todo: need query with projectId!
        return taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    protected Sort getDefaultSort() {
        return Sort.by("id");
    }

    protected Page<TaskEntity> findAllByProjectId(String projectId, Pageable pageable) {
        // todo: need query with projectId!
        return taskRepository.findAll(pageable);
    }

    protected TaskEntity newEntity(String projectId, TaskDto dto) {
        ProjectEntity parent = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException());

        TaskEntity entity = converter.newEntity(dto);
        // todo: need implementation
        // entity.setParent(parent.getId());
        return entity;
    }

}
