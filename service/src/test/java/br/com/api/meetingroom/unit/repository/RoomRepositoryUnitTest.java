package br.com.api.meetingroom.unit.repository;

import br.com.api.meetingroom.core.BaseRepositoryUnitTest;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static org.assertj.core.api.Assertions.assertThat;


public class RoomRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private RoomRepository roomRepository;


    @Test
    void shouldFindByIdAndActiveWithSuccess() {
        Room roomDB = roomRepository.save(newRoomBuilder().id(1L).active(true).build());

        Optional<Room> room = roomRepository.findByIdAndActive(1l, true);

        assertThat(room).isPresent();
        assertThat(roomDB.getActive()).isEqualTo(room.get().getActive());
        assertThat(roomDB.getName()).isEqualTo(room.get().getName());
    }

    @Test
    void shouldReturnOptionalEmpty(){
        Room roomDB = roomRepository.save(newRoomBuilder().id(1L).active(false).build());

        Optional<Room> room = roomRepository.findByIdAndActive(1l, true);

        assertThat(room).isEmpty();
    }


    @Test
    void shouldFindByNameWithSuccess() {

        Room roomSaved = roomRepository.save(newRoomBuilder().name("Sala Dev").build());

        Optional<Room> room = roomRepository.findByName(roomSaved.getName());

        assertThat(room).isNotNull();
        assertThat(roomSaved.getName()).isEqualTo(room.get().getName());

    }

    @Test
    void testFindRoomWhenNameNotExist() {
        Optional<Room> room = roomRepository.findByName("Sala 0");

        assertThat(room).isEmpty();
    }


    @Test
    @Transactional
    void shouldDeactivateRoomWithSuccess(){
        Room roomDB = roomRepository.save(newRoomBuilder().build());

        roomRepository.deactivate(roomDB.getId());
       Optional<Room> room = roomRepository.findById(roomDB.getId());

       assertThat(room).isPresent().containsInstanceOf(Room.class);
       assertThat(room.get().getActive()).isFalse();
       assertThat(roomDB.getId()).isEqualTo(room.get().getId());
    }

    @Test
    @Transactional
    void shouldActivateRoomWithSuccess(){
        Room roomDB = roomRepository.save(newRoomBuilder().active(false).build());

        roomRepository.activate(roomDB.getId());
        Optional<Room> room = roomRepository.findById(roomDB.getId());

        assertThat(room).isPresent().containsInstanceOf(Room.class);
        assertThat(room.get().getActive()).isTrue();
        assertThat(roomDB.getId()).isEqualTo(room.get().getId());
    }

    @Test
    @Transactional
    void shouldUpdateRoomWithSuccess(){
        String roomName = "Sala Atualizada";
        Integer seats = 35;
        Room roomDB = roomRepository.save(newRoomBuilder().seats(20).build());

        roomRepository.updateRoom(roomDB.getId(),roomName,seats );

        Optional<Room> optionalRoom = roomRepository.findById(roomDB.getId());

        assertThat(optionalRoom).isPresent();
        assertThat(optionalRoom.get().getSeats()).isEqualTo(seats);
        assertThat(optionalRoom.get().getName()).isEqualTo(roomName);
    }

    @Test
    void testFindAllWinthFilter(){
        List<Room> rooms = Arrays.asList(
                newRoomBuilder().build(),
                newRoomBuilder().build(),
                newRoomBuilder().active(false).build()
        );
        roomRepository.saveAll(rooms);

        List<Room> roomList = roomRepository.findAllWinthFilter(null, true, PageRequest.of(0, 2));

        assertThat(roomList).hasSize(2);
        assertThat(roomList).contains(rooms.get(0));
        assertThat(roomList).contains(rooms.get(1));
        assertThat(roomList).doesNotContain(rooms.get(2));


    }

}
