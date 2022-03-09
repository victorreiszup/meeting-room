package br.com.api.meetingroom.domain.entity;

import br.com.api.meetingroom.util.DateUltils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;


import static br.com.api.meetingroom.util.DateUltils.*;
import static java.util.Objects.isNull;

@Entity
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Embedded
    private Employee employee;

    private String subject;

    private OffsetDateTime startAt;

    private OffsetDateTime endAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public Allocation() {
    }

    private Allocation(Long id, Room room, Employee employee, String subject, OffsetDateTime startAt, OffsetDateTime endAt, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
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
            createdAt = newOffsetDateTimeNow();
        }
    }

    @PreUpdate
    void preUpdate() {
        if (isNull(updatedAt)) {
            updatedAt = newOffsetDateTimeNow();
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

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
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
        private OffsetDateTime startAt;
        private OffsetDateTime endAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

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

        public AllocationBuilder startAt(OffsetDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public AllocationBuilder endAt(OffsetDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public AllocationBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AllocationBuilder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Allocation build() {
            return new Allocation(id, room, employee, subject, startAt, endAt, createdAt, updatedAt);
        }
    }
}
