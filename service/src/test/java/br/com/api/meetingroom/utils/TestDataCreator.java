package br.com.api.meetingroom.utils;

import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Employee;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;

import java.time.LocalDateTime;

import static br.com.api.meetingroom.util.DateUltils.newLocalDateTimeNow;

public final class TestDataCreator {

    public static final long DEFAULT_ROOM_ID = 1L;
    public static final String DEFAULT_ROOM_NAME = "Room A";
    public static final int DEFAULT_ROOM_SEATS = 6;

    public static final long DEFAULT_ALLOCATION_ID = 1L;
    public static final String DEFAULT_SUBJECT = "Assunto x";
    public static final String DEFAULT_EMPLOYEE_NAME = "João Silva";
    public static final String DEFAULT_EMPLOYEE_EMAIL = "silva@email.com";
    public static final LocalDateTime DEFAULT_ALLOCATION_START_AT = newLocalDateTimeNow();
    public static final LocalDateTime DEFAULT_ALLOCATION_END_AT = DEFAULT_ALLOCATION_START_AT.plusHours(1);


    private TestDataCreator() {}

    public static Room.RoomBuilder newRoomBuilder(){
        return Room.newRoomBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }

    public static Allocation.AllocationBuilder newAllocationBuilder(Room room){
        return Allocation.newAllocationBuilder()
                .id(DEFAULT_ALLOCATION_ID)
                .room(room)
                .employee(Employee.newEmployeeBuilder().name(DEFAULT_EMPLOYEE_NAME).email(DEFAULT_EMPLOYEE_EMAIL).build())
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }

    public static CreatedRoomDTO.RoomDTOBuilder newCreatedRoomDToBuilder(){
        return  CreatedRoomDTO.newCreatedRoomDtoBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }

    public static UpdateRoomDTO.RoomDTOBuilder newUpdateRoomDtoBuilder(){
        return UpdateRoomDTO.newUpdateRoomDtoBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateAllocationDTO.CreateAllocationDTOBuilder newCreateAllocationDtoBuilder(){
        return CreateAllocationDTO.newCreateAllocationDTOBuilder()
                .roomId(DEFAULT_ROOM_ID)
                .subject(DEFAULT_SUBJECT)
                .employeeName(DEFAULT_EMPLOYEE_NAME)
                .employeeEmail(DEFAULT_EMPLOYEE_EMAIL)
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);

    }

    public static UpdateAllocationDTO.UpdateAllocationDTOBuilder newUpadateAllocationDToBuilder(){
        return UpdateAllocationDTO.newUpdateAllocationDToBuilder()
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }
}
