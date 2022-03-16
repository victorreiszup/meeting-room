package br.com.api.meetingroom.util;

import br.com.api.meetingroom.exception.InvalidRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

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
        Sort definedSort = parseOrderByFields(orderBy, validFields);
        return PageRequest.of(definedPage, definedLimit, definedSort);
    }

    private static Sort parseOrderByFields(String orderBy, List<String> validSortableFields) {

        if (isNull(validSortableFields) || validSortableFields.isEmpty()) {
            throw new IllegalArgumentException("No valid sortable fields were defined");
        }
        if (isNull(orderBy) || orderBy.trim().isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orderList =
                Stream
                        .of(orderBy.split(","))
                        .map(
                                element -> {
                                    String fieldName;
                                    Sort.Order order;

                                    if (element.startsWith("-")) {
                                        fieldName = element.substring(1);
                                        order = Sort.Order.desc(fieldName);
                                    } else {
                                        fieldName = element;
                                        order = Sort.Order.asc(fieldName);
                                    }
                                    if (!validSortableFields.contains(fieldName)) {
                                        throw new InvalidRequestException("Invalid sort field");
                                    }

                                    return order;
                                }
                        ).collect(Collectors.toList());

        return Sort.by(orderList);
    }
}
