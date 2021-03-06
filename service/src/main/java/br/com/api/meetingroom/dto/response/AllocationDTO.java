package br.com.api.meetingroom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AllocationDTO {

    private Long id;

    private Long roomId;

    private String employeeName;

    private String employeeEmail;

    private String subject;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endAt;

    public AllocationDTO(Long id, Long roomId, String employeeName, String employeeEmail, String subject, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.roomId = roomId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.subject = subject;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Long getId() {
        return id;
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

    public static AllocationDTOBuilder newAllocationDTO() {
        return new AllocationDTOBuilder();
    }

    public static final class AllocationDTOBuilder {
        private Long id;
        private Long roomId;
        private String employeeName;
        private String employeeEmail;
        private String subject;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        private AllocationDTOBuilder() {
        }

        public AllocationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AllocationDTOBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public AllocationDTOBuilder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public AllocationDTOBuilder employeeEmail(String employeeEmail) {
            this.employeeEmail = employeeEmail;
            return this;
        }

        public AllocationDTOBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public AllocationDTOBuilder startAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public AllocationDTOBuilder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public AllocationDTO build() {
            return new AllocationDTO(id, roomId, employeeName, employeeEmail, subject, startAt, endAt);
        }
    }
}
