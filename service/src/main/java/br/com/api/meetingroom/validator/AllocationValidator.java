package br.com.api.meetingroom.validator;

import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.exception.BusinessException;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.util.DateUltils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;

@Component
public class AllocationValidator {

    private final AllocationRepository allocationRepository;

    public AllocationValidator(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    private final int ALLOCATION_MAX_DURATION_SECONDS = 4 * 60 * 60;

    public void validate(CreateAllocationDTO createAllocationDTO) {
        validateDate(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());
        validateDuration(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());
        validateIfTimeAvailable(createAllocationDTO.getRoomId(), createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt());

    }

    private void validateDate(OffsetDateTime starAt, OffsetDateTime endAt) {
        if (starAt.isEqual(endAt) || starAt.isAfter(endAt)) {
            throw new ConflictException("Datetime iconsistent");
        }
    }

    private void validateDuration(OffsetDateTime starAt, OffsetDateTime endAt) {
        if (Duration.between(starAt, endAt).getSeconds() > ALLOCATION_MAX_DURATION_SECONDS) {
            throw new BusinessException("Allocation exceeds duration");
        }
    }

    private void validateIfTimeAvailable(Long roomId, OffsetDateTime startAt, OffsetDateTime endAt) {
        allocationRepository.findAllWithFilter(roomId, DateUltils.newOffsetDateTimeNow(), endAt)
                .stream()
                .filter(a -> DateUltils.isOverlapping(startAt, endAt, a.getStartAt(), a.getEndAt()))
                .findFirst()
                .ifPresent(x -> {throw new ConflictException("Allocation overlap");});
    }


}
