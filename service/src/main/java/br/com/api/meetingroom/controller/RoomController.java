package br.com.api.meetingroom.controller;

import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;
import br.com.api.meetingroom.dto.response.RoomDTO;
import br.com.api.meetingroom.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @Operation(summary = "Criar uma sala")
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody CreatedRoomDTO createdRoomDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(createdRoomDTO));
    }

    @Operation(summary = "Buscar informações de uma sala através do seu id")
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> findRoombyId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.findRoomById(id));
    }

    @Operation(summary = "Desativa uma sala através do seu id")
    @PutMapping("/{id}/deactive")
    public ResponseEntity<Void> deactivateRoom(@PathVariable(value = "id") Long id) {
        roomService.deactivateRoom(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Ativa uma sala através do seu id")
    @PutMapping("/{id}/active")
    public ResponseEntity<Void> activateRoom(@PathVariable(value = "id") Long id) {
        roomService.activateRoom(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Atualizar uma sala")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRoom(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateRoomDTO updateRoomDTO) {
        roomService.updateRoom(id, updateRoomDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
