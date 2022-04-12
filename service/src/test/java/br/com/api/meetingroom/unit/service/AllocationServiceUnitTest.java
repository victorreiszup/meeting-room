package br.com.api.meetingroom.unit.service;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.response.AllocationDTO;
import br.com.api.meetingroom.exception.InvalidRequestException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.service.AllocationService;
import br.com.api.meetingroom.util.DateUltils;
import br.com.api.meetingroom.utils.MapperUtils;
import br.com.api.meetingroom.utils.TestDataCreator;
import br.com.api.meetingroom.validator.AllocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static br.com.api.meetingroom.util.DateUltils.newLocalDateTimeNow;
import static br.com.api.meetingroom.utils.TestDataCreator.newAllocationBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newUpadateAllocationDToBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        Exception exception = assertThrows(NotFoundException.class, () -> allocationService.deleteAllocation(1l));
        assertNotNull(exception);
        assertEquals("Allocation not found", exception.getMessage());
    }

    @Test
    void shouldFailDeletePastAllocation() {
        Room room = newRoomBuilder().build();

        Allocation pastAllocation = newAllocationBuilder(room)
                .startAt(newLocalDateTimeNow().minusHours(1))
                .endAt(newLocalDateTimeNow().minusMinutes(30))
                .build();

        when(allocationRepository.findById(pastAllocation.getId())).thenReturn(Optional.of(pastAllocation));

        Exception exception = assertThrows(InvalidRequestException.class, () -> allocationService.deleteAllocation(pastAllocation.getId()));
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
        Allocation allocation = newAllocationBuilder(room).endAt(newLocalDateTimeNow().minusSeconds(15)).build();
        when(allocationRepository.findById(anyLong())).thenReturn(Optional.of(allocation));

        Exception exception = assertThrows(InvalidRequestException.class,
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
                        .startAt(newLocalDateTimeNow().plusHours(1).truncatedTo(ChronoUnit.MINUTES))
                        .endAt(newLocalDateTimeNow().plusHours(2).truncatedTo(ChronoUnit.MINUTES))
                        .build()
        );

        verify(allocationRepository)
                .updateAllocation(
                        allocation.getId(),
                        newLocalDateTimeNow().plusHours(1).truncatedTo(ChronoUnit.MINUTES),
                        newLocalDateTimeNow().plusHours(2).truncatedTo(ChronoUnit.MINUTES),
                        newLocalDateTimeNow()
                );
    }

    @Test
    void testGetAllocationWithSuccess() {
        Room room = newRoomBuilder().build();
        Allocation allocation = newAllocationBuilder(room).build();
        when(allocationRepository.findById(allocation.getId())).thenReturn(Optional.of(allocation));

        AllocationDTO allocationDTO = allocationService.getAllocation(allocation.getId());

        assertEquals(allocation.getId(), allocationDTO.getId());
        assertEquals(allocation.getEmployee().getName(), allocationDTO.getEmployeeName());
        verify(allocationRepository).findById(allocation.getId());
    }

    @Test
    void testGetAllocationWithFail() {
        Exception exception = assertThrows(NotFoundException.class,
                () -> allocationService.getAllocation(1L));

        assertEquals("Allocation not found", exception.getMessage());
    }


    @Test
    void shouldCreateAllocationWithSuccess() {
        Room room = newRoomBuilder().id(1L).active(true).build();
        CreateAllocationDTO createAllocationDTO = TestDataCreator.newCreateAllocationDtoBuilder().roomId(room.getId()).build();
        when(roomRepository.findByIdAndActive(room.getId(), true)).thenReturn(Optional.of(room));

        AllocationDTO allocationDTO = allocationService.createAllocation(createAllocationDTO);

        assertThat(allocationDTO).isNotNull().matches(e -> e.getRoomId().equals(room.getId()));
        assertThat(createAllocationDTO.getEmployeeName()).isEqualTo(allocationDTO.getEmployeeName());

        assertThat(allocationDTO.getStartAt())
                .isEqualTo(DateUltils.newLocalDateTimeNow())
                .isBefore(allocationDTO.getEndAt());

        assertThat(allocationDTO.getEndAt())
                .isAfter(DateUltils.newLocalDateTimeNow());

        verify(allocationRepository).save(any());

    }

}
