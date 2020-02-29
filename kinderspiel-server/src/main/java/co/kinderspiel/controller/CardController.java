package co.kinderspiel.controller;

import co.kinderspiel.converter.CardConverter;
import co.kinderspiel.dto.CardDto;
import co.kinderspiel.model.CardEntity;
import co.kinderspiel.model.ProjectEntity;
import co.kinderspiel.repository.CardRepository;
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
@RequestMapping("/api/project/{projectId}/card")
@RequiredArgsConstructor
public class CardController implements BaseController {

    private final CardRepository cardRepository;

    private final ProjectRepository projectRepository;

    private final CardConverter converter;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PagedModel<CardDto> find(@PathVariable("projectId") String projectId, @RequestParam(required = false) MultiValueMap<String, String> params) {
        Page<CardEntity> entities = findAllByProjectId(projectId, parsePageRequest(params, getDefaultSort()));
        List<CardDto> content = converter.fromEntities(entities.getContent());
        content.forEach(c -> {
            c.add(linkTo(methodOn(CardController.class).getEntity(c.getProjectId(), c.getId())).withSelfRel());
        });
        return HateoasHelper.buildPageable(content, entities, params, CardController.class, projectId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public CardDto getById(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        CardEntity entity = getEntity(projectId, id);
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CardDto create(@PathVariable("projectId") String projectId, @RequestBody @NotNull @Validated CardDto dto) {
        CardEntity entity = cardRepository.save(newEntity(projectId, dto));
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CardDto update(@PathVariable("projectId") String projectId, @PathVariable String id, @RequestBody @NotNull @Validated CardDto dto) {
        CardEntity entity = getEntity(projectId, id);
        converter.updateEntityFromEdit(dto, entity);
        cardRepository.save(entity);
        return addLinks(converter.fromEntity(entity));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable("projectId") String projectId, @PathVariable("id") String id) {
        CardEntity entity = getEntity(projectId, id);
        cardRepository.delete(entity);
    }

    protected CardEntity getEntity(String projectId, String id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    protected Sort getDefaultSort() {
        return Sort.by("id");
    }

    protected Page<CardEntity> findAllByProjectId(String projectId, Pageable pageable) {
        return cardRepository.findByProjectId(projectId, pageable);
    }

    protected CardEntity newEntity(String projectId, CardDto dto) {
        ProjectEntity parent = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException());

        CardEntity entity = converter.newEntity(dto);
        entity.setProjectId(entity.getId());
        return entity;
    }

    private CardDto addLinks(CardDto dto) {
        dto.add(linkTo(methodOn(CardController.class, dto.getProjectId()).getById(dto.getProjectId(), dto.getId())).withRel(IanaLinkRelations.EDIT));
        return dto;
    }

}
