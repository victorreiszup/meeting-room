package br.com.api.meetingroom.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Room {
    public static final List<String> SORTABLE_FIELDS = Arrays.asList("name","seats");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer seats;

    private Boolean active;

    public Room() {
    }

    private Room(Long id, String name, Integer seats, Boolean active) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public Boolean getActive() {
        return active;
    }

    @PrePersist
    void prePersist() {
        if (Objects.isNull(active)) {
            active = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(name, room.name) && Objects.equals(seats, room.seats) && Objects.equals(active, room.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seats, active);
    }

    public static RoomBuilder newRoomBuilder() {
        return new RoomBuilder();
    }

    public static final class RoomBuilder {
        private Long id;
        private String name;
        private Integer seats;
        private Boolean active;

        private RoomBuilder() {
        }

        public RoomBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoomBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoomBuilder seats(Integer seats) {
            this.seats = seats;
            return this;
        }

        public RoomBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Room build() {
            return new Room(id, name, seats, active);
        }
    }
}
