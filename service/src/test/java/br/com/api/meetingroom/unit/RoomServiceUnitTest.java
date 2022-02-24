package br.com.api.meetingroom.unit;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.ConflictException;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.service.RoomService;
import br.com.api.meetingroom.utils.MapperUtils;
import br.com.api.meetingroom.utils.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomServiceUnitTest extends BaseUnitTest {

    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        roomService = new RoomService(roomRepository, MapperUtils.roomMapper());
    }


    @Test
    void findRoomByIdSuccess() {
        Room room = TestDataCreator.newRoomBuilder().id(1l).build();
        when(roomRepository.findByIdAndActive(1L, true)).thenReturn(Optional.of(room));

        RoomDTO roomDTO = roomService.findRoomById(1L);

        assertEquals(room.getId(), roomDTO.getId());
        assertEquals(room.getName(), roomDTO.getName());
        assertEquals(room.getSeats(), roomDTO.getSeats());
    }


    @Test
    void findRoomByIdNotFound() {

        when(roomRepository.findByIdAndActive(1L, true)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> roomService.findRoomById(1L));
    }

    @Test
    void testCreateRoomSuccess() {
        CreatedRoomDTO createdRoomDTO = TestDataCreator.newCreatedRoomDToBuilder().build();

        RoomDTO roomDTO = roomService.createRoom(createdRoomDTO);

        assertEquals(createdRoomDTO.getName(), roomDTO.getName());
        assertEquals(createdRoomDTO.getSeats(), roomDTO.getSeats());
        verify(roomRepository).save(any());

    }

    @Test
    void testDeleteRoomSuccess(){
        Room room = TestDataCreator.newRoomBuilder().id(1L).build();
        when(roomRepository.findByIdAndActive(room.getId(), true)).thenReturn(Optional.of(room));

        roomService.deleteRoom(room.getId());

        verify(roomRepository).deactivate(any());
    }


    @Test
    void testUpdateRoomSuccess() {
        UpdateRoomDTO updateRoomDTO = TestDataCreator.newUpdateRoomDtoBuilder().build();
        Room room = TestDataCreator.newRoomBuilder().id(1L).active(true).name(updateRoomDTO.getName()).build();
        when(roomRepository.findByIdAndActive(1L, true)).thenReturn(Optional.of(room));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));

        roomService.updateRoom(1L, updateRoomDTO);

        assertEquals(room.getName(), updateRoomDTO.getName());
        verify(roomRepository).updateRoom(any(),any(),any());

    }

    @Test
    void testUpdateRoomDuplicateName() {
        UpdateRoomDTO updateRoomDTO = TestDataCreator.newUpdateRoomDtoBuilder().build();
        Room room = TestDataCreator.newRoomBuilder().id(2L).active(true).build();
        when(roomRepository.findByIdAndActive(1L, true)).thenReturn(Optional.of(room));
        when(roomRepository.findByName(updateRoomDTO.getName())).thenReturn(Optional.of(room));

        assertThrows(ConflictException.class, ()->roomService.updateRoom(1L, updateRoomDTO));

    }

}
