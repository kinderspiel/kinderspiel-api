package co.kinderspiel.until;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class HateoasHelper {

    public static PagedModel.PageMetadata convert(Page page) {
        return new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }

    public static UriComponentsBuilder uriBuilder(MultiValueMap<String, String> params, Pageable pageable) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        if (params != null) {
            builder.queryParams(params);
        }
        if (pageable != null) {
            builder.replaceQueryParam("page", pageable.getPageNumber());
            builder.replaceQueryParam("size", pageable.getPageSize());
        }
        return builder;
    }

    public static <T> PagedModel<T> buildPageable(List<T> content, Page page, MultiValueMap<String, String> params, Class clazz, Object... clazzParam) {
        PagedModel<T> pagedModel = new PagedModel(content, HateoasHelper.convert(page));
        pagedModel.add(linkTo(clazz, clazzParam).slash(HateoasHelper.uriBuilder(params, page.getPageable()).toUriString()).withSelfRel());
        if (page.hasPrevious()) {
            pagedModel.add(linkTo(clazz, clazzParam).slash(HateoasHelper.uriBuilder(params, page.previousPageable()).toUriString()).withRel(IanaLinkRelations.PREV));
        }
        if (page.hasNext()) {
            pagedModel.add(linkTo(clazz, clazzParam).slash(HateoasHelper.uriBuilder(params, page.nextPageable()).toUriString()).withRel(IanaLinkRelations.NEXT));
        }
        pagedModel.add(linkTo(clazz, clazzParam).withRel(IanaLinkRelations.EDIT));
        return pagedModel;
    }
}
