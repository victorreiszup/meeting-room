package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.response.AllocationDTO;
import br.com.api.meetingroom.exception.AllocationCannotDeletedException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.AllocationMapper;
import br.com.api.meetingroom.util.DateUltils;
import br.com.api.meetingroom.validator.AllocationValidator;
import org.springframework.stereotype.Service;

@Service
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final RoomRepository roomRepository;
    private final AllocationMapper allocationMapper;
    private final AllocationValidator allocationValidator;

    public AllocationService(AllocationRepository allocationRepository, RoomRepository roomRepository, AllocationMapper allocationMapper, AllocationValidator allocationValidator) {
        this.allocationRepository = allocationRepository;
        this.roomRepository = roomRepository;
        this.allocationMapper = allocationMapper;
        this.allocationValidator = allocationValidator;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {

        Room room = roomRepository.findByIdAndActive(createAllocationDTO.getRoomId(), true)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        allocationValidator.validate(createAllocationDTO);

        Allocation allocation = allocationMapper.fromCreateAllocationDtoToEntity(createAllocationDTO, room);

        allocationRepository.save(allocation);

        return allocationMapper.fromEntityToAllocationDTO(allocation);
    }

    public void deleteAllocation(Long allocationId) {

        Allocation allocation = getAllocationOrThrowException(allocationId);

        if (isPastAllocation(allocation)) {
            throw new AllocationCannotDeletedException("Cannot delete allocation in the past");
        }

        allocationRepository.delete(allocation);
    }

    private Allocation getAllocationOrThrowException(Long allocationId) {
        return allocationRepository.findById(allocationId).orElseThrow(() -> new NotFoundException("Allocation not found"));
    }

    private boolean isPastAllocation(Allocation allocation) {
        return allocation.getEndAt().isBefore(DateUltils.newOffsetDateTimeNow());
    }


}
