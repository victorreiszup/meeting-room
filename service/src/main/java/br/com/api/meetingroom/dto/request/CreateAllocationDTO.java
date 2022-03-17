package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreateAllocationDTO {

    @NotNull
    private Long roomId;

    @NotBlank
    private String employeeName;

    @NotBlank
    @Email
    private String employeeEmail;

    @NotBlank
    private String subject;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    public CreateAllocationDTO(Long roomId, String employeeName, String employeeEmail, String subject, LocalDateTime startAt, LocalDateTime endAt) {
        this.roomId = roomId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.subject = subject;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
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
        CreateAllocationDTO that = (CreateAllocationDTO) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(employeeName, that.employeeName) && Objects.equals(employeeEmail, that.employeeEmail) && Objects.equals(subject, that.subject) && Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, employeeName, employeeEmail, subject, startAt, endAt);
    }

    public static CreateAllocationDTOBuilder newCreateAllocationDTOBuilder() {
        return new CreateAllocationDTOBuilder();
    }

    public static final class CreateAllocationDTOBuilder {
        private Long roomId;
        private String employeeName;
        private String employeeEmail;
        private String subject;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        private CreateAllocationDTOBuilder() {
        }

        public CreateAllocationDTOBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public CreateAllocationDTOBuilder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public CreateAllocationDTOBuilder employeeEmail(String employeeEmail) {
            this.employeeEmail = employeeEmail;
            return this;
        }

        public CreateAllocationDTOBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public CreateAllocationDTOBuilder startAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public CreateAllocationDTOBuilder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public CreateAllocationDTO build() {
            return new CreateAllocationDTO(roomId, employeeName, employeeEmail, subject, startAt, endAt);
        }
    }
}
