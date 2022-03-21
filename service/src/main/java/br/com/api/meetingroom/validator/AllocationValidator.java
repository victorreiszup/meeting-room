package br.com.api.meetingroom.validator;

import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.exception.BusinessException;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.util.DateUltils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static br.com.api.meetingroom.util.DateUltils.*;
import static br.com.api.meetingroom.util.DateUltils.isOverlapping;

@Component
public class AllocationValidator {

    private final AllocationRepository allocationRepository;

    public AllocationValidator(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    private final int ALLOCATION_MAX_DURATION_SECONDS = 4 * 60 * 60;

    public void validateCreatedAllocation(CreateAllocationDTO createAllocationDTO) {
        validateDate(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());
        validateDateInTheFuture(createAllocationDTO.getStartAt());
        validateDuration(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());
        validateIfTimeAvailable(createAllocationDTO.getRoomId(), createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());

    }

    public void validateUpdateAllocation(Long roomId, UpdateAllocationDTO updateAllocationDTO) {
        validateDate(updateAllocationDTO.getStartAt(), updateAllocationDTO.getEndAt());
        validateDateInTheFuture(updateAllocationDTO.getStartAt());
        validateDuration(updateAllocationDTO.getStartAt(), updateAllocationDTO.getEndAt());
        validateIfTimeAvailable(roomId, updateAllocationDTO.getStartAt(), updateAllocationDTO.getEndAt());
    }

    private void validateDate(LocalDateTime starAt, LocalDateTime endAt) {
        if (starAt.isEqual(endAt) || starAt.isAfter(endAt)) {
            throw new ConflictException("Datetime iconsistent");
        }
    }

    private void validateDateInTheFuture(LocalDateTime starAt) {
        if (starAt.isBefore(newLocalDateTimeNow())) {
            throw new ConflictException("Date in the past");
        }
    }

    private void validateDuration(LocalDateTime starAt, LocalDateTime endAt) {
        if (Duration.between(starAt, endAt).getSeconds() > ALLOCATION_MAX_DURATION_SECONDS) {
            throw new BusinessException("Allocation exceeds duration");
        }
    }

    private void validateIfTimeAvailable(Long roomId, LocalDateTime startAt, LocalDateTime endAt) {
        allocationRepository.findAllWithFilter(roomId, LocalDateTime.now(), endAt)
                .stream()
                .filter(a -> isOverlapping(startAt, endAt, a.getStartAt(), a.getEndAt()))
                .findFirst()
                .ifPresent(x -> {
                    throw new ConflictException("Allocation overlap");
                });
    }


}
