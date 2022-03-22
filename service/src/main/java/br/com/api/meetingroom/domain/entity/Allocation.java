package br.com.api.meetingroom.domain.entity;


import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static br.com.api.meetingroom.util.DateUltils.newLocalDateTimeNow;
import static java.util.Objects.isNull;

@Entity
public class Allocation {
    public static final List<String> SORTABLE_FIELDS = Arrays.asList("startAt", "endAt");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Embedded
    private Employee employee;

    private String subject;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Allocation() {
    }

    private Allocation(Long id, Room room, Employee employee, String subject, LocalDateTime startAt, LocalDateTime endAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.room = room;
        this.employee = employee;
        this.subject = subject;
        this.startAt = startAt;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    void prePersist() {
        if (isNull(createdAt)) {
            createdAt = newLocalDateTimeNow();
        }
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Employee getEmployee() {
        return employee;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static AllocationBuilder newAllocationBuilder() {
        return new AllocationBuilder();
    }


    public static final class AllocationBuilder {
        private Long id;
        private Room room;
        private Employee employee;
        private String subject;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private AllocationBuilder() {
        }


        public AllocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AllocationBuilder room(Room room) {
            this.room = room;
            return this;
        }

        public AllocationBuilder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public AllocationBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public AllocationBuilder startAt(LocalDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public AllocationBuilder endAt(LocalDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public AllocationBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AllocationBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Allocation build() {
            return new Allocation(id, room, employee, subject, startAt, endAt, createdAt, updatedAt);
        }
    }
}
