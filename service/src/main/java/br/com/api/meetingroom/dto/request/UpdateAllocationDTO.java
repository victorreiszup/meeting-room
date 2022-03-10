package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;

public class UpdateAllocationDTO {

    @NotBlank
    private String subject;

    @NotNull
    @FutureOrPresent
    private OffsetDateTime startAt;

    @NotNull
    @FutureOrPresent
    private OffsetDateTime endAt;

    public UpdateAllocationDTO(String subject, OffsetDateTime startAt, OffsetDateTime endAt) {
        this.subject = subject;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public String getSubject() {
        return subject;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
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
        private OffsetDateTime startAt;
        private OffsetDateTime endAt;

        private UpdateAllocationDTOBuilder() {
        }


        public UpdateAllocationDTOBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public UpdateAllocationDTOBuilder startAt(OffsetDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public UpdateAllocationDTOBuilder endAt(OffsetDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public UpdateAllocationDTO build() {
            return new UpdateAllocationDTO(subject, startAt, endAt);
        }
    }
}
