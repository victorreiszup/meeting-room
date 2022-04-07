package br.com.api.meetingroom.mapper;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    public abstract RoomDTO fromEntityToDTO(Room room);

    public abstract Room fromDToToEntity(CreatedRoomDTO createdRoomDTO);
}
