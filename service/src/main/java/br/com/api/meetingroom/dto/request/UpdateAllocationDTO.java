package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

public class UpdateAllocationDTO {

    @NotBlank
    private String subject;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    public UpdateAllocationDTO(String subject, LocalDateTime startAt, LocalDateTime endAt) {
        this.subject = subject;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public String getSubject() {
        return subject;
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
        return Objects.equals(subject, that.subject) && Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, startAt, endAt);
    }

    public static UpdateAllocationDTOBuilder newUpdateAllocationDToBuilder() {
        return new UpdateAllocationDTOBuilder();
    }

    public static final class UpdateAllocationDTOBuilder {
        private String subject;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        private UpdateAllocationDTOBuilder() {
        }


        public UpdateAllocationDTOBuilder subject(String subject) {
            this.subject = subject;
            return this;
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
            return new UpdateAllocationDTO(subject, startAt, endAt);
        }
    }
}
