package br.com.api.meetingroom.controller;

import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.dto.response.AllocationDTO;
import br.com.api.meetingroom.service.AllocationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/allocations")
@Api(tags = "api/v1/allocations")
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

    @Operation(summary = "Listar todas as alocações")
    @GetMapping
    public ResponseEntity<List<AllocationDTO>> listAllocations(
            @RequestParam(required = false) String employeeEmail,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAnt,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer page) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(allocationService.listAllocations(employeeEmail, roomId, startAt, endAnt, orderBy, limit, page));
    }

    @Operation(summary = "Buscar por uma alocação")
    @GetMapping("/{id}")
    public ResponseEntity<AllocationDTO> findAllocation(@PathVariable(value = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(allocationService.getAllocation(id));
    }
}
