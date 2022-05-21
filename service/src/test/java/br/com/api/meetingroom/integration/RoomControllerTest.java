package br.com.api.meetingroom.integration;

import br.com.api.meetingroom.core.BaseTest;
import br.com.api.meetingroom.domain.entity.Room;
import br.com.api.meetingroom.domain.repository.RoomRepository;
import br.com.api.meetingroom.dto.request.CreatedRoomDTO;
import br.com.api.meetingroom.dto.request.UpdateRoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static br.com.api.meetingroom.utils.TestDataCreator.newCreatedRoomDToBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newRoomBuilder;
import static br.com.api.meetingroom.utils.TestDataCreator.newUpdateRoomDtoBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
class RoomControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    void shouldCreateRoomWithSucess() throws Exception {

        CreatedRoomDTO createdRoomDTO = newCreatedRoomDToBuilder().build();

        String payload = mapper.writeValueAsString(createdRoomDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Room A"));

        List<Room> rooms = roomRepository.findAll();
        assertEquals(1, rooms.size());
    }

    @Test
    void shouldNotCreateRoomWithInvalidData() throws Exception {
        CreatedRoomDTO createdRoomDTO = newCreatedRoomDToBuilder().name(null).seats(null).build();

        String payload = mapper.writeValueAsString(createdRoomDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("não deve estar em branco")).isTrue();
    }

    @Test
    void shouldFindRoomByIdWithSuccess() throws Exception {
        Room room = newRoomBuilder().build();
        room = roomRepository.save(room);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rooms/" + room.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Room A"))
                .andExpect(jsonPath("$.active").value(true));

        assertThat(roomRepository.findById(room.getId())).isPresent();
        assertThat(roomRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    void shouldReturnNotFoundWhenTheRoomDoNotExist() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rooms/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Room not found"));

        assertThat(roomRepository.findAll()).size().isEqualTo(0);
    }

    @Test
    void shouldDisableRoomWithSuccess() throws Exception {
        Room room = newRoomBuilder().build();
        room = roomRepository.save(room);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/rooms/" + room.getId() + "/deactive")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful());

        assertThat(roomRepository.findById(room.getId())).isPresent();
        assertThat(roomRepository.findById(room.getId()).get().getActive()).isFalse();

    }

    @Test
    void shouldNotDisableRoomWhenDoNotExist() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/rooms/" + 100 + "/deactive")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Room not found"));
    }

    @Test
    void shouldActivateTheRoomWithSuccess() throws Exception {
        Room room = roomRepository.save(newRoomBuilder().active(false).build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rooms/" + room.getId() + "/active")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(roomRepository.findById(room.getId())
                .get()
                .getActive())
                .isEqualTo(true);
    }

    @Test
    void shouldNotActivateRoomWhenNotExist() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rooms/" + 100 + "/active")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateRoomWithSuccess() throws Exception {
        Room room = roomRepository.save(newRoomBuilder().build());
        UpdateRoomDTO updateRoomDTO = newUpdateRoomDtoBuilder().name("Test room").seats(42).build();

        String payload = mapper.writeValueAsString(updateRoomDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rooms/" + room.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        List<Room> rooms = roomRepository.findAll();
        assertEquals(1, rooms.size());

    }

    @Test
    void shouldNotUpdateRoomWhenNoExist() throws Exception {

        UpdateRoomDTO updateRoomDTO = newUpdateRoomDtoBuilder().name("Test room").seats(42).build();

        String payload = mapper.writeValueAsString(updateRoomDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/rooms/" + 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Room not found"));
    }

    @Test
    void shouldListAllRoom() throws Exception {
        List<Room> rooms = Arrays.asList(
                newRoomBuilder().build(),
                newRoomBuilder().build(),
                newRoomBuilder().build()
                );
        roomRepository.saveAll(rooms);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/rooms")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(roomRepository.findAll()).size().isEqualTo(rooms.size());
    }
}