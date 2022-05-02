package br.com.api.meetingroom.service;

import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.mapper.RoomMapper;
import br.com.api.meetingroom.util.PageUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Service
public class RoomService {

    private static final int MAX_LIMIT_ELEMENT = 10;

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomDTO createRoom(CreatedRoomDTO createdRoomDTO) {
        validateNameDuplicate(null, createdRoomDTO.getName());
        Room room = roomMapper.fromDToToEntity(createdRoomDTO);
        roomRepository.save(room);
        return roomMapper.fromEntityToDTO(room);
    }

    public RoomDTO findRoomById(Long id) {
        Room room = getRoomOrThrowException(id);
        return roomMapper.fromEntityToDTO(room);
    }

    @Transactional
    public void deactivateRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("Room not found");
        }
        roomRepository.deactivate(id);
    }

    @Transactional
    public void activateRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("Room not found");
        }
        roomRepository.activate(id);
    }


    @Transactional
    public void updateRoom(Long id, UpdateRoomDTO updateRoomDTO) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("Room not found");
        }
        validateNameDuplicate(id, updateRoomDTO.getName());
        roomRepository.updateRoom(id, updateRoomDTO.getName(), updateRoomDTO.getSeats());
    }

    public List<RoomDTO> listRooms(String name, Boolean active, String oderBy, Integer limit, Integer page) {
        Pageable pageable = PageUtils.newPageable(page, limit, MAX_LIMIT_ELEMENT, oderBy, Room.SORTABLE_FIELDS);
        List<Room> rooms = roomRepository.findAllWinthFilter(name, active, pageable);
        return rooms.stream().map(roomMapper::fromEntityToDTO).collect(Collectors.toList());
    }

    private Room getRoomOrThrowException(Long id) {
        return roomRepository.findById(id)
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
