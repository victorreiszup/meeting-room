package br.com.api.meetingroom.dto.response;

public class RoomDTO {

    private Long id;

    private String name;

    private Integer seats;

    private Boolean active;

    public RoomDTO(Long id, String name, Integer seats, Boolean active) {
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

    public static RoomDTOBuilder newRoomDtoBuilder() {
        return new RoomDTOBuilder();
    }

    public static final class RoomDTOBuilder {
        private Long id;
        private String name;
        private Integer seats;
        private Boolean active;

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

        public RoomDTOBuilder active(Boolean active){
            this.active = active;
            return this;
        }

        public RoomDTO build() {
            return new RoomDTO(id, name, seats, active);
        }
    }

}
