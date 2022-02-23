package br.com.api.meetingroom.unit;

import br.com.api.meetingroom.core.BaseUnitTest;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.exception.NotFoundException;
import br.com.api.meetingroom.service.RoomService;
import br.com.api.meetingroom.utils.MapperUtils;
import br.com.api.meetingroom.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertEquals(room.getName(),roomDTO.getName());
        assertEquals(room.getSeats(),roomDTO.getSeats());
    }


    @Test
    void findRoomByIdNotFound() {

        when(roomRepository.findByIdAndActive(1L, true)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->roomService.findRoomById(1L));
    }


}
