package br.com.api.meetingroom.unit.pageUtils;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.exception.InvalidRequestException;
import br.com.api.meetingroom.util.PageUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PageUtilsUnitTes extends BaseUnitTest {

    private final int MAX_LIMIT = 10;

    @Test
    void newPageableWhenPageIsNullAndLimitIsNullAndOrderByIsNull() {
        Pageable pageable = PageUtils.newPageable(null, null, MAX_LIMIT, null, Allocation.SORTABLE_FIELDS);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPageIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> PageUtils.newPageable(-1, null, MAX_LIMIT, null, Allocation.SORTABLE_FIELDS));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenLimitElementIsInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> PageUtils.newPageable(null, 0, MAX_LIMIT, null, Allocation.SORTABLE_FIELDS));
        assertThat(exception.getMessage()).isEqualTo("Page size must not be less than one!");
    }

    @Test
    void newPageableWhenLimitExceedsMaximum() {
        Pageable pageable = PageUtils.newPageable(null, 11, MAX_LIMIT, null, Allocation.SORTABLE_FIELDS);
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidSortableFieldsIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> PageUtils.newPageable(null, 0, MAX_LIMIT, null, null));
        assertEquals("No valid sortable fields were defined", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidSortableFieldsIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> PageUtils.newPageable(null, 0, MAX_LIMIT, null, Collections.EMPTY_LIST));
        assertEquals("No valid sortable fields were defined", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFieldIsInvalid() {
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> PageUtils.newPageable(null, 0, MAX_LIMIT, "xpto", Allocation.SORTABLE_FIELDS));
        assertEquals("Invalid sort field", exception.getMessage());
    }

    @Test
    void newPageableWhenOrdeByAscIsValid() {
        Pageable pageable = PageUtils.newPageable(null, 8, MAX_LIMIT, Allocation.SORTABLE_FIELDS.get(0), Allocation.SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.asc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void newPageableWhenOrdeByDescIsValid() {
        Pageable pageable = PageUtils.newPageable(null, 8, 10, "-" + Allocation.SORTABLE_FIELDS.get(0), Allocation.SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.desc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

}
