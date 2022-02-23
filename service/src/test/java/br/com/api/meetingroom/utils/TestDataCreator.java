package br.com.api.meetingroom.utils;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;

public final class TestDataCreator {

    public static final long DEFAULT_ROOM_ID = 1L;
    public static final String DEFAULT_ROOM_NAME = "Room A";
    public static final int DEFAULT_ROOM_SEATS = 6;

    private TestDataCreator() {}

    public static Room.RoomBuilder newRoomBuilder(){
        return Room.newRoomBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }

    public static CreatedRoomDTO.RoomDTOBuilder newCreatedRoomDToBuilder(){
        return  CreatedRoomDTO.newCreatedRoomDtoBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }
}
