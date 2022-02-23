package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class CreatedRoomDTO {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer seats;

    public CreatedRoomDTO( String name, Integer seats) {

        this.name = name;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public static RoomDTOBuilder newCreatedRoomDtoBuilder() {
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

        public CreatedRoomDTO build() {
            return new CreatedRoomDTO( name, seats);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatedRoomDTO that = (CreatedRoomDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(seats, that.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, seats);
    }
}
