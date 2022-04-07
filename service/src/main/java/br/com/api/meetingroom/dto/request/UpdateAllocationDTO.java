package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class UpdateAllocationDTO {

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    public UpdateAllocationDTO(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateAllocationDTO that = (UpdateAllocationDTO) o;
        return Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt, endAt);
    }

    public static UpdateAllocationDTOBuilder newUpdateAllocationDToBuilder() {
        return new UpdateAllocationDTOBuilder();
    }

    public static final class UpdateAllocationDTOBuilder {
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        private UpdateAllocationDTOBuilder() {
        }

        public UpdateAllocationDTOBuilder startAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public UpdateAllocationDTOBuilder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public UpdateAllocationDTO build() {
            return new UpdateAllocationDTO(startAt, endAt);
        }
    }
}
