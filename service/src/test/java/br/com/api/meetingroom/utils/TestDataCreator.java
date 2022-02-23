package br.com.api.meetingroom.utils;

import br.com.api.meetingroom.domain.entity.Room;

public final class TestDataCreator {
    private TestDataCreator() {}

    public static Room.RoomBuilder newRoomBuilder(){
        return Room.newRoomBuilder()
                .name("Room 001")
                .seats(20);
    }
}
