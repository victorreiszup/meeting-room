package br.com.api.meetingroom.integration;

import br.com.api.meetingroom.core.BaseTest;
import br.com.api.meetingroom.domain.entity.Allocation;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.AllocationRepository;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreateAllocationDTO;
import br.com.api.meetingroom.dto.request.UpdateAllocationDTO;
import br.com.api.meetingroom.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static br.com.api.meetingroom.utils.TestDataCreator.newAllocationBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newCreateAllocationDtoBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newUpadateAllocationDToBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
class AllocationControllerTest extends BaseTest {
    private final Long INVALID_ID = 1234L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private AllocationRepository allocationRepository;

    @Test
    void shouldCreateAllocationWithSuccess() throws Exception {
        Room room = roomRepository.save(newRoomBuilder().build());
        CreateAllocationDTO createAllocationDTO = newCreateAllocationDtoBuilder()
                .roomId(room.getId())
                .startAt(DateUtils.newLocalDateTimeNow().plusHours(1))
                .endAt(DateUtils.newLocalDateTimeNow().plusHours(2))
                .build();

        String payLoad = mapper.writeValueAsString(createAllocationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/allocations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payLoad);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeName").value("João Silva"));

        assertEquals(1, allocationRepository.findAll().size());

    }

    @Test
    void shouldNotCreateAllocationWithInvalidRoom() throws Exception {

        CreateAllocationDTO createAllocationDTO = newCreateAllocationDtoBuilder().roomId(INVALID_ID).build();

        String payLoad = mapper.writeValueAsString(createAllocationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/allocations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payLoad);

        String response = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response.contains("Room not available for allocation")).isTrue();
    }


    @Test
    void shouldDeleteAllocationWithSuccess() throws Exception {
        Room room = roomRepository.save(newRoomBuilder().build());
        Allocation allocation = allocationRepository.save(newAllocationBuilder(room).build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/allocations/" + allocation.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(allocationRepository.findAll().isEmpty()).isTrue();

    }

    @Test
    void shouldReturnMessageWhenAllocationNotExist() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/allocations/" + INVALID_ID);

        String response = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response.contains("Allocation not found")).isTrue();
    }

    @Test
    void shouldUpdateAllocationWithSuccess() throws Exception {
        Room room = roomRepository.save(newRoomBuilder().build());
        Allocation allocation = allocationRepository.save(newAllocationBuilder(room).build());

        UpdateAllocationDTO updateAllocationDTO = newUpadateAllocationDToBuilder()
                .startAt(LocalDateTime.now().plusDays(1))
                .endAt(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        String payLoad = mapper.writeValueAsString(updateAllocationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/allocations/" + allocation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(allocationRepository.findById(allocation.getId())).isPresent();

    }

    @Test
    void shouldListAllAllocations() throws Exception {
        Room room = newRoomBuilder().build();
        roomRepository.save(room);
        allocationRepository.save(newAllocationBuilder(room).build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/allocations")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

    }


    @Test
    void shouldGetOneAllocation() throws Exception {
        Room room = newRoomBuilder().build();
        roomRepository.save(room);
        Allocation allocation = allocationRepository.save(newAllocationBuilder(room).build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/allocations/" + allocation.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

    }

}