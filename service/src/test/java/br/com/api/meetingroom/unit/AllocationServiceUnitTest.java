package br.com.api.meetingroom.unit;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.exception.AllocationCannotDeletedException;
import br.com.api.meetingroom.exception.AllocationCannotUpdateException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.service.AllocationService;
import br.com.api.meetingroom.utils.MapperUtils;
import br.com.api.meetingroom.utils.TestDataCreator;
import br.com.api.meetingroom.validator.AllocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static br.com.api.meetingroom.util.DateUltils.newOffsetDateTimeNow;
import static br.com.api.meetingroom.utils.TestDataCreator.*;
import static br.com.api.meetingroom.utils.TestDataCreator.newAllocationBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllocationServiceUnitTest extends BaseUnitTest {

    private AllocationService allocationService;

    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private AllocationValidator allocationValidator;

    @BeforeEach
    void setupEach() {
        allocationService = new AllocationService(allocationRepository, roomRepository, MapperUtils.allocationMapper(), allocationValidator);
    }

    @Test
    void shouldFailWhenAllocationDoesNotExist() {
        when(allocationRepository.findById(1l)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> allocationService.deleteAllocation(1l));
        assertNotNull(exception);
        assertEquals("Allocation not found", exception.getMessage());
    }

    @Test
    void shouldFailDeletePastAllocation() {
        Room room = newRoomBuilder().build();

        Allocation pastAllocation = newAllocationBuilder(room)
                .startAt(newOffsetDateTimeNow().minusHours(1))
                .endAt(newOffsetDateTimeNow().minusMinutes(30))
                .build();

        when(allocationRepository.findById(pastAllocation.getId())).thenReturn(Optional.of(pastAllocation));

        Exception exception = assertThrows(AllocationCannotDeletedException.class, () -> allocationService.deleteAllocation(pastAllocation.getId()));
        assertNotNull(exception);
        assertEquals("Cannot delete allocation in the past", exception.getMessage());

    }

    @Test
    void testDeleteAllocationSuccess() {
        Room room = newRoomBuilder().build();
        Allocation allocation = newAllocationBuilder(room).build();
        when(allocationRepository.findById(allocation.getId())).thenReturn(Optional.of(allocation));

        allocationService.deleteAllocation(allocation.getId());

        verify(allocationRepository).delete(allocation);
    }

    @Test
    void shouldFailToUpdateAllocationDoesNotExist() {

        when(allocationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class,
                () -> allocationService.upadateAllocation(1L, newUpadateAllocationDToBuilder().build()));

        assertNotNull(exception);
        assertEquals("Allocation not found", exception.getMessage());
    }

    @Test
    void shouldFailToUpdatePastAllocation() {
        Room room = newRoomBuilder().build();
        Allocation allocation = newAllocationBuilder(room).endAt(newOffsetDateTimeNow().minusSeconds(15)).build();
        when(allocationRepository.findById(anyLong())).thenReturn(Optional.of(allocation));

        Exception exception = assertThrows(AllocationCannotUpdateException.class,
                () -> allocationService.upadateAllocation(1L, newUpadateAllocationDToBuilder().build()));

        assertNotNull(exception);
        assertEquals("Cannot update a finalized allocation", exception.getMessage());
    }

    @Test
    void shouldUpdateAllocationWithSuccess() {
        Room room = newRoomBuilder().build();
        Allocation allocation = newAllocationBuilder(room).build();

        when(allocationRepository.findById(allocation.getId())).thenReturn(Optional.of(allocation));

        allocationService.upadateAllocation(
                allocation.getId(),
                newUpadateAllocationDToBuilder()
                        .subject("Daily Devs")
                        .startAt(newOffsetDateTimeNow().plusHours(1).truncatedTo(ChronoUnit.MINUTES))
                        .endAt(newOffsetDateTimeNow().plusHours(2).truncatedTo(ChronoUnit.MINUTES))
                        .build()
        );

        verify(allocationRepository)
                .updateAllocation(
                        allocation.getId(),
                        "Daily Devs",
                        newOffsetDateTimeNow().plusHours(1).truncatedTo(ChronoUnit.MINUTES),
                        newOffsetDateTimeNow().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
    }
}