package br.com.api.meetingroom.dto.response;

import java.util.Objects;

public class RoomDTO {

    private Long id;

    private String name;

    private Integer seats;

    public RoomDTO(Long id, String name, Integer seats) {
        this.id = id;
        this.name = name;
        this.seats = seats;
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

    public static RoomDTOBuilder newRoomDtoBuilder() {
        return new RoomDTOBuilder();
    }

    public static final class RoomDTOBuilder {
        private Long id;
        private String name;
        private Integer seats;

        private RoomDTOBuilder() {
        }


        public RoomDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoomDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoomDTOBuilder seats(Integer seats) {
            this.seats = seats;
            return this;
        }

        public RoomDTO build() {
            return new RoomDTO(id, name, seats);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDTO roomDTO = (RoomDTO) o;
        return Objects.equals(id, roomDTO.id) && Objects.equals(name, roomDTO.name) && Objects.equals(seats, roomDTO.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seats);
    }
}
