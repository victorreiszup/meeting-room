package br.com.api.meetingroom.unit.pageUtils;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.exception.InvalidRequestException;
import br.com.api.meetingroom.util.PageUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PageUtilsUnitTes extends BaseUnitTest {


    @Test
    void newPageableWhenPageIsNullAndLimitIsNullAndOrderByIsNull(){
        Pageable pageable = PageUtils.newPageable(null,null,10,null, Allocation.SORTABLE_FIELDS);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPageIsNegative(){
        assertThrows(IllegalArgumentException.class,
                ()-> PageUtils.newPageable(-1,null,10,null, Allocation.SORTABLE_FIELDS));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenLimitIsInvalid(){
        assertThrows(IllegalArgumentException.class,
                ()-> PageUtils.newPageable(null,0,10,null, Allocation.SORTABLE_FIELDS));
    }

    @Test
    void newPageableWhenLimitExceedsMaximum(){
        Pageable pageable = PageUtils.newPageable(null,11,10,null, Allocation.SORTABLE_FIELDS);
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidSortableFieldsIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> PageUtils.newPageable(null,0,10,null, null));
        assertEquals("No valid sortable fields were defined", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidSortableFieldsIsEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> PageUtils.newPageable(null,0,10,null, Collections.EMPTY_LIST));
        assertEquals("No valid sortable fields were defined", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFieldIsInvalid(){
        Exception exception = assertThrows(InvalidRequestException.class,
                ()-> PageUtils.newPageable(null,0,10,"xpto", Allocation.SORTABLE_FIELDS));
        assertEquals("Invalid sort field", exception.getMessage());
    }

    @Test
    void newPageableWhenOrdeByAscIsValid(){
        Pageable pageable = PageUtils.newPageable(null,8,10,Allocation.SORTABLE_FIELDS.get(0), Allocation.SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.asc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void newPageableWhenOrdeByDescIsValid(){
        Pageable pageable = PageUtils.newPageable(null,8,10,"-"+Allocation.SORTABLE_FIELDS.get(0), Allocation.SORTABLE_FIELDS);
        assertEquals(Sort.by(Sort.Order.desc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

}
