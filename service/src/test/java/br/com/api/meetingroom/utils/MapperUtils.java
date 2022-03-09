package br.com.api.meetingroom.utils;

import br.com.api.meetingroom.mapper.AllocationMapper;
import br.com.api.meetingroom.mapper.RoomMapper;
import org.mapstruct.factory.Mappers;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static RoomMapper roomMapper() {
        return Mappers.getMapper(RoomMapper.class);
    }

    public static AllocationMapper allocationMapper() {
        return Mappers.getMapper(AllocationMapper.class);
    }


}
