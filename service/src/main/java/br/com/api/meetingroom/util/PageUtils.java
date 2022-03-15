package br.com.api.meetingroom.util;

import antlr.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;

public final class PageUtils {

    private PageUtils() {
    }

    public static Pageable newPageable(
            Integer page,
            Integer limit,
            int maxLimit,
            String orderBy,
            List<String> validFields
    ) {
        int definedPage = isNull(page) ? 0 : page;
        int definedLimit = isNull(limit) ? maxLimit : Math.min(limit, maxLimit);
        Sort definedSort = parseOrderByFields(orderBy,validFields);
        return PageRequest.of(definedPage,definedLimit,definedSort);
    }

    private static Sort parseOrderByFields(String orderBy, List<String> validSortableFields){

        if(isNull(validSortableFields) || validSortableFields.isEmpty()){
            throw new IllegalArgumentException("No valid sortable fields were defined");
        }
        return null;
    }
}
