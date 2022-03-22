package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.dto.response.AllocationDTO;
import br.com.api.meetingroom.exception.InvalidRequestException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.AllocationMapper;
import br.com.api.meetingroom.util.PageUtils;
import br.com.api.meetingroom.validator.AllocationValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.api.meetingroom.util.DateUltils.newLocalDateTimeNow;
import static java.util.Objects.isNull;

@Service
public class AllocationService {
    private static final int MAX_LIMIT_ELEMENT = 10;

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
                .orElseThrow(() -> new InvalidRequestException("Room not available for allocation"));

        allocationValidator.validateCreatedAllocation(createAllocationDTO);

        Allocation allocation = allocationMapper.fromCreateAllocationDtoToEntity(createAllocationDTO, room);

         allocationRepository.save(allocation);

        return allocationMapper.fromEntityToAllocationDTO(allocation);
    }

    public void deleteAllocation(Long allocationId) {

        Allocation allocation = getAllocationOrThrowException(allocationId);

        if (isPastAllocation(allocation)) {
            throw new InvalidRequestException("Cannot delete allocation in the past");
        }

        allocationRepository.delete(allocation);
    }

    @Transactional
    public void upadateAllocation(Long allocationId, UpdateAllocationDTO updateAllocationDTO) {

        Allocation allocation = getAllocationOrThrowException(allocationId);

        if (isPastAllocation(allocation)) {
            throw new InvalidRequestException("Cannot update a finalized allocation");
        }

        allocationValidator.validateUpdateAllocation(allocation.getRoom().getId(), updateAllocationDTO);

        allocationRepository.updateAllocation(
                allocationId,
                updateAllocationDTO.getStartAt(),
                updateAllocationDTO.getEndAt(),
                newLocalDateTimeNow()
        );

    }

    public List<AllocationDTO> listAllocations(String employeeEmail, Long roomId, LocalDate startAt,
                                               LocalDate endAt, String orderBy, Integer limit, Integer page) {

        Pageable pageable = PageUtils.newPageable(page, limit, MAX_LIMIT_ELEMENT, orderBy, Allocation.SORTABLE_FIELDS);

        List<Allocation> allocations = allocationRepository.findAllWithFilter(
                employeeEmail,
                roomId,
                isNull(startAt) ? null : startAt.atTime(LocalTime.MIN),
                isNull(endAt) ? null : endAt.atTime(LocalTime.MAX),
                pageable
        );

        return allocations
                .stream()
                .map(allocationMapper::fromEntityToAllocationDTO)
                .collect(Collectors.toList());
    }

    private Allocation getAllocationOrThrowException(Long allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() -> new NotFoundException("Allocation not found"));
    }

    private boolean isPastAllocation(Allocation allocation) {
        return allocation.getEndAt().isBefore(newLocalDateTimeNow());
    }

}
