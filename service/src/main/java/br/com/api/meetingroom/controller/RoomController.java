package br.com.api.meetingroom.controller;

import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> findRoombyId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.findRoomById(id));
    }
}
