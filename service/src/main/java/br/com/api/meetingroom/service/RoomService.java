package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.RoomMapper;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomDTO findRoomById(Long id) {
        Room room = getActiveRoomOrThrowException(id);
        return roomMapper.fromEntityToDTO(room);
    }

    private Room getActiveRoomOrThrowException(Long id) {
        return roomRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }
}
