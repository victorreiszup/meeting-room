package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

import static java.util.Objects.*;

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
        validateNameDuplicate(null, createdRoomDTO.getName());
        Room room = roomMapper.fromDToToEntity(createdRoomDTO);
        roomRepository.save(room);
        return roomMapper.fromEntityToDTO(room);
    }

    @Transactional
    public void deactivateRoom(Long id) {
        getActiveRoomOrThrowException(id);
        roomRepository.deactivate(id);
    }

    @Transactional
    public void activateRoom(Long id) {
        roomRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Room not found"));
        roomRepository.activate(id);
    }


    @Transactional
    public void updateRoom(Long id, UpdateRoomDTO updateRoomDTO) {
        getActiveRoomOrThrowException(id);
        validateNameDuplicate(id, updateRoomDTO.getName());
        roomRepository.updateRoom(id, updateRoomDTO.getName(), updateRoomDTO.getSeats());

    }

    private Room getActiveRoomOrThrowException(Long id) {
        return roomRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

    private void validateNameDuplicate(Long idRoom, String name) {
        roomRepository.findByName(name)
                .ifPresent(room -> {
                    if (isNull(idRoom) || !Objects.equals(room.getId(), idRoom)) {
                        throw new ConflictException("Room - Duplicate name");
                    }
                });
    }

}
