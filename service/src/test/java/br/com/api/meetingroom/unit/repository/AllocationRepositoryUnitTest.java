package br.com.api.meetingroom.unit.repository;

import br.com.api.meetingroom.core.BaseRepositoryUnitTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static br.com.api.meetingroom.util.DateUltils.newLocalDateTimeNow;
import static br.com.api.meetingroom.utils.TestDataCreator.DEFAULT_ALLOCATION_END_AT;
import static br.com.api.meetingroom.utils.TestDataCreator.DEFAULT_ALLOCATION_START_AT;
import static br.com.api.meetingroom.utils.TestDataCreator.newAllocationBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static org.assertj.core.api.Assertions.assertThat;


public class AllocationRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void shouldFindAllAllocationWithFilter() {
        Room room = newRoomBuilder().build();
        room = roomRepository.save(room);
        Allocation allocation = newAllocationBuilder(room).build();
        allocationRepository.save(allocation);

        List<Allocation> allocations = allocationRepository.findAllWithFilter(room.getId(), allocation.getStartAt(), allocation.getEndAt());

        assertThat(allocations)
                .isNotEmpty()
                .hasSize(1);
        assertThat(allocations.get(0).equals(allocation));

    }

    @Test
    void shouldNotReturnAllocationListWithElement(){
        Room room = newRoomBuilder().build();
        room = roomRepository.save(room);
        Allocation allocation = newAllocationBuilder(room).build();
        allocationRepository.save(allocation);

        List<Allocation> allocations = allocationRepository.findAllWithFilter(room.getId(), allocation.getStartAt().plusMinutes(1), allocation.getEndAt().plusMinutes(1));

        assertThat(allocations).isEmpty();

    }

    @Test
    @Transactional
    void shouldUpdateAllocation(){
        Room room = newRoomBuilder().build();
        room = roomRepository.save(room);
        Allocation allocation = newAllocationBuilder(room).build();
        allocation = allocationRepository.save(allocation);

        allocationRepository.updateAllocation(allocation.getId(), DEFAULT_ALLOCATION_START_AT.plusHours(4),DEFAULT_ALLOCATION_END_AT.plusHours(5), newLocalDateTimeNow());

        Optional<Allocation> allocationOptional = allocationRepository.findById(allocation.getId());
        assertThat(allocationOptional).isPresent();
        assertThat(allocationOptional.get().getStartAt().equals(DEFAULT_ALLOCATION_START_AT.plusHours(4)));
        assertThat(allocationOptional.get().getEndAt().equals(DEFAULT_ALLOCATION_END_AT.plusHours(5)));

    }



}
