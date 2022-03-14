package br.com.api.meetingroom.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


public class UpdateRoomDTO {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer seats;

    public UpdateRoomDTO(String name, Integer seats) {

        this.name = name;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public static RoomDTOBuilder newUpdateRoomDtoBuilder() {
        return new RoomDTOBuilder();
    }

    public static final class RoomDTOBuilder {

        private String name;
        private Integer seats;

        private RoomDTOBuilder() {
        }

        public RoomDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoomDTOBuilder seats(Integer seats) {
            this.seats = seats;
            return this;
        }

        public UpdateRoomDTO build() {
            return new UpdateRoomDTO(name, seats);
        }
    }

}
