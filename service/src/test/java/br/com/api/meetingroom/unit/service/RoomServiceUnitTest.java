package br.com.api.meetingroom.unit.service;

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

import static br.com.api.meetingroom.utils.TestDataCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Room room = newRoomBuilder().id(1l).build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomDTO roomDTO = roomService.findRoomById(1L);

        assertEquals(room.getId(), roomDTO.getId());
        assertThat(room.getName()).isEqualTo(roomDTO.getName());
        assertThat(roomDTO.getSeats()).isEqualTo(room.getSeats());

    }


    @Test
    void findRoomByIdNotFound() {
        assertThatThrownBy(() -> roomService.findRoomById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Room not found");
    }

    @Test
    void testCreateRoomSuccess() {
        CreatedRoomDTO createdRoomDTO = newCreatedRoomDToBuilder().build();

        RoomDTO roomDTO = roomService.createRoom(createdRoomDTO);

        assertEquals(createdRoomDTO.getName(), roomDTO.getName());
        assertEquals(createdRoomDTO.getSeats(), roomDTO.getSeats());
        verify(roomRepository).save(any());

    }

    @Test
    void testDeactivateRoomWithSuccess() {
        Room room = newRoomBuilder().id(1L).build();
        when(roomRepository.existsById(room.getId())).thenReturn(true);

        roomService.deactivateRoom(room.getId());


        verify(roomRepository).deactivate(room.getId());
    }

    @Test
    void shouldNotDeactiveWhenRoomNotExist() {
        when(roomRepository.existsById(1L)).thenReturn(false);
        assertThatThrownBy(() -> roomService.deactivateRoom(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Room not found");
    }

    @Test
    void testActivateRoomWithSuccess() {
        Room room = newRoomBuilder().id(1L).active(false).build();
        when(roomRepository.existsById(room.getId())).thenReturn(true);

        roomService.activateRoom(room.getId());


        verify(roomRepository).activate(room.getId());
    }

    @Test
    void shouldNotActivateWhenRoomNotExist() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> roomService.activateRoom(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Room not found");
    }


    @Test
    void testUpdateRoomSuccess() {
        UpdateRoomDTO updateRoomDTO = newUpdateRoomDtoBuilder().build();
        Room room = newRoomBuilder().id(1L).active(true).name(updateRoomDTO.getName()).build();
        when(roomRepository.existsById(room.getId())).thenReturn(true);

        roomService.updateRoom(1L, updateRoomDTO);

        assertEquals(room.getName(), updateRoomDTO.getName());
        verify(roomRepository).updateRoom(any(), any(), any());

    }

    @Test
    void shouldNotUpdateWhenRoomNotExist() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> roomService.updateRoom(1L, newUpdateRoomDtoBuilder().build()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Room not found");
    }

}
