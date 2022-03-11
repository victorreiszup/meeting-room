package br.com.api.meetingroom.controller;

import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.dto.response.AllocationDTO;
import br.com.api.meetingroom.service.AllocationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/allocations")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @Operation(summary = "Realizar uma alocação")
    @PostMapping
    public ResponseEntity<AllocationDTO> createAllocation(@Valid @RequestBody CreateAllocationDTO createAllocationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(allocationService.createAllocation(createAllocationDTO));
    }

    @Operation(summary = "Deletar uma alocação através do seu id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllocation(@PathVariable(value = "id") Long id) {
        allocationService.deleteAllocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Atualizar informações de uma alocação")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAllocation(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateAllocationDTO updateAllocationDTO) {
        allocationService.upadateAllocation(id, updateAllocationDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
