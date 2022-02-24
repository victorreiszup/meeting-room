package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public RoomDTO createRoom(CreatedRoomDTO createdRoomDTO) {
        Room room = roomMapper.fromDToToEntity(createdRoomDTO);
        roomRepository.save(room);
        return roomMapper.fromEntityToDTO(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        getActiveRoomOrThrowException(id);
        roomRepository.deactivate(id);
    }

    private Room getActiveRoomOrThrowException(Long id) {
        return roomRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

}
