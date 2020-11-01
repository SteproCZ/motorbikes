package cz.upce.nnpro.motorbikes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import cz.upce.nnpro.motorbikes.service.MotorbikeService;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest(controllers = MotorbikeController.class)
@ActiveProfiles("test")
class MotorbikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MotorbikeService motorbikeService;

    private List<Motorbike> motorbikes;

    @BeforeEach
    void setUp() {
        this.motorbikes = new ArrayList<>();
        this.motorbikes.add(new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0));
        this.motorbikes.add(new Motorbike(2, "Kawasaki", "KLE 500", 2007, 27000));
        this.motorbikes.add(new Motorbike(3, "Yamaha", "XT 660 Z Tenere", 2015,30000));

    }

    @Test
    void shouldFetchAllMotorbikes() throws Exception {
        BDDMockito.given(motorbikeService.getAllMotorbikes()).willReturn(motorbikes);

        this.mockMvc.perform(get("/motorbikes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(motorbikes.size())));
    }

    @Test
    void shouldFetchOneMotorbikeById() throws Exception {
        final int motorbikeId = 1;
        final Motorbike motorbike = new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0);

        BDDMockito.given(motorbikeService.getMotorbike(motorbikeId)).willReturn(motorbike);

        this.mockMvc.perform(get("/motorbike/{idMotorbike}", motorbikeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make", CoreMatchers.is(motorbike.getMake())))
                .andExpect(jsonPath("$.model", CoreMatchers.is(motorbike.getModel())))
                .andExpect(jsonPath("$.year", CoreMatchers.is(motorbike.getYear())));
    }

    @Test
    void shouldReturn404WhenFindMotorbikeByIdMotorbike() throws Exception {
        final int motorbikeId = 1;
        BDDMockito.given(motorbikeService.getMotorbike(motorbikeId)).willReturn(null);

        this.mockMvc.perform(get("/motorbike/{idMotorbike}", motorbikeId))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateNewMotorbike() throws Exception {
        final Motorbike motorbike = new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0);

        BDDMockito.given(motorbikeService.createMotorbike(ArgumentMatchers.any(MotorbikeDto.class))).willReturn(motorbike);

        this.mockMvc.perform(post("/motorbike")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motorbike)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make", CoreMatchers.is(motorbike.getMake())))
                .andExpect(jsonPath("$.model", CoreMatchers.is(motorbike.getModel())))
                .andExpect(jsonPath("$.year", CoreMatchers.is(motorbike.getYear())))
                .andExpect(jsonPath("$.mileage", CoreMatchers.is(motorbike.getMileage())));
    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutModel() throws Exception {
        final Motorbike motorbike = new Motorbike(1, "BMW", null, 2020, 0);

        this.mockMvc.perform(post("/motorbike")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motorbike)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutMake() throws Exception {
        final Motorbike motorbike = new Motorbike(1, null, "R 1250 GS Adventure", 2020, 0);

        this.mockMvc.perform(post("/motorbike")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motorbike)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        int motorbikeId = 1;
        final Motorbike motorbike = new Motorbike(motorbikeId, "BMW", "R 1250 GS Adventure", 2020, 0);
        BDDMockito.given(motorbikeService.getMotorbike(motorbikeId)).willReturn(motorbike);
        BDDMockito.given(motorbikeService.editMotorbike(ArgumentMatchers.any(MotorbikeDto.class), ArgumentMatchers.any(int.class))).willReturn(motorbike);

        this.mockMvc.perform(put("/motorbike/{idMotorbike}", motorbike.getIdMotorbike())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motorbike)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make", CoreMatchers.is(motorbike.getMake())))
                .andExpect(jsonPath("$.model", CoreMatchers.is(motorbike.getModel())))
                .andExpect(jsonPath("$.year", CoreMatchers.is(motorbike.getYear())))
                .andExpect(jsonPath("$.mileage", CoreMatchers.is(motorbike.getMileage())));

    }

    @Test
    void shouldDeleteMotorbike() throws Exception {
        int motorbikeId = 1;
        final Motorbike motorbike = new Motorbike(motorbikeId, "BMW", "R 1250 GS Adventure", 2020, 0);
        BDDMockito.given(motorbikeService.getMotorbike(motorbikeId)).willReturn(motorbike);
        Mockito.doNothing().when(motorbikeService).removeMotorbike(motorbike.getIdMotorbike());

        this.mockMvc.perform(delete("/{idMotorbike}", motorbike.getIdMotorbike()))
                .andExpect(status().isNoContent());

    }
}