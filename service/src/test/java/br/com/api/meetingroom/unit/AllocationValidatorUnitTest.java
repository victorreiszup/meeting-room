package br.com.api.meetingroom.unit;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.exception.BusinessException;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.util.DateUltils;
import br.com.api.meetingroom.validator.AllocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static br.com.api.meetingroom.domain.entity.Allocation.newAllocationBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newCreateAllocationDtoBuilder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AllocationValidatorUnitTest extends BaseUnitTest {

    private AllocationValidator allocationValidator;

    @Mock
    private AllocationRepository allocationRepository;


    @BeforeEach
    void setupEach() {
        allocationValidator = new AllocationValidator(allocationRepository);
    }


    @Test
    void testValidateWhenHourIsInvalid() {
        Exception exception = assertThrows(
                ConflictException.class,
                () -> allocationValidator.validateCreatedAllocation(
                        newCreateAllocationDtoBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow())
                                .endAt(DateUltils.newOffsetDateTimeNow())
                                .build()
                ));

        assertEquals("Datetime iconsistent", exception.getMessage());
    }


    @Test
    void testValidateWhenStarAtIsInvalid() {
        Exception exception = assertThrows(
                ConflictException.class,
                () -> allocationValidator.validateCreatedAllocation(
                        newCreateAllocationDtoBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow().plusMinutes(2))
                                .endAt(DateUltils.newOffsetDateTimeNow())
                                .build()
                ));

        assertEquals("Datetime iconsistent", exception.getMessage());
    }


    @Test
    void testValidateWhenDateIsValid() {
        assertDoesNotThrow(() -> allocationValidator.validateCreatedAllocation(
                newCreateAllocationDtoBuilder()
                        .startAt(DateUltils.newOffsetDateTimeNow())
                        .endAt(DateUltils.newOffsetDateTimeNow().plusHours(1))
                        .build()
        ));
    }

    @Test
    void testValidateWhenDurationIsInvalid() {
        Exception exception = assertThrows(
                BusinessException.class,
                () -> allocationValidator.validateCreatedAllocation(
                        newCreateAllocationDtoBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow())
                                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(4).plusSeconds(1))
                                .build()
                ));
        assertEquals("Allocation exceeds duration", exception.getMessage());
    }

    @Test
    void testValidateWhenDurationIsValid() {
        assertDoesNotThrow(() ->
                allocationValidator.validateCreatedAllocation(
                        newCreateAllocationDtoBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow())
                                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(4))
                                .build()
                ));
    }

    @Test
    void testValidateIfTimeAllocationUnavailable() {

        CreateAllocationDTO createAllocationDTO = newCreateAllocationDtoBuilder()
                .startAt(DateUltils.newOffsetDateTimeNow().minusHours(1))
                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(1))
                .build();

        when(allocationRepository.findAllWithFilter(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        newAllocationBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow())
                                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(1))
                                .build()
                ));

           Exception exception = assertThrows(ConflictException.class,
                    ()-> allocationValidator.validateCreatedAllocation(createAllocationDTO));

           assertNotNull(exception);
           assertEquals("Allocation overlap", exception.getMessage());
    }

    @Test
    void testValidateIfTimeAllocationAvailable() {

        CreateAllocationDTO createAllocationDTO = newCreateAllocationDtoBuilder()
                .startAt(DateUltils.newOffsetDateTimeNow().plusHours(1).plusSeconds(20))
                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(3))
                .build();

        when(allocationRepository.findAllWithFilter(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        newAllocationBuilder()
                                .startAt(DateUltils.newOffsetDateTimeNow())
                                .endAt(DateUltils.newOffsetDateTimeNow().plusHours(1))
                                .build()
                ));

        assertDoesNotThrow(() -> allocationValidator.validateCreatedAllocation(createAllocationDTO));
    }


}
