package cz.upce.nnpro.motorbikes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.nnpro.motorbikes.controller.MotorbikeController;
import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import cz.upce.nnpro.motorbikes.repository.MotorbikeRepository;
import cz.upce.nnpro.motorbikes.service.MotorbikeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
public class MotorbikeIntTests {

    @Autowired
    private MotorbikeController motorbikeController;

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    private List<Motorbike> motorbikes;

    private int size;

    private MotorbikeDto motorbikeDto;

    @BeforeEach
    void setUp() {
        motorbikeDto = new MotorbikeDto("BMW", "R 1250 GS Adventure", 2020, 0);

        size = motorbikeRepository.findAll().size();
        this.motorbikes = new ArrayList<>();
        this.motorbikes.add(new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0));
        this.motorbikes.add(new Motorbike(2, "Kawasaki", "KLE 500", 2007, 27000));
        this.motorbikes.add(new Motorbike(3, "Yamaha", "XT 660 Z Tenere", 2015,30000));
        motorbikes = motorbikeRepository.saveAll(motorbikes);
    }

    @Test
    void shouldFetchAllMotorbikes() throws Exception {
        assertEquals(size + 3, motorbikeController.getMotorbikes().getBody().size());
    }

    @Test
    void shouldFetchOneMotorbikeById() throws Exception {
        Motorbike motorbike = motorbikeController.getMotorbike(motorbikes.get(0).getIdMotorbike()).getBody();
        assertEquals(motorbikes.get(0).getMake(), motorbike.getMake());
        assertEquals(motorbikes.get(0).getModel(), motorbike.getModel());
        assertEquals(motorbikes.get(0).getYear(), motorbike.getYear());
        assertEquals(motorbikes.get(0).getIdMotorbike(), motorbike.getIdMotorbike());
        assertEquals(motorbikes.get(0).getMileage(), motorbike.getMileage());
    }

    @Test
    void shouldCreateNewMotorbikeAndFind() throws Exception {
        Motorbike motorbike = motorbikeController.createMotorbike(motorbikeDto).getBody();
        assertEquals(motorbikeDto.getMake(), motorbike.getMake());
        assertEquals(motorbikeDto.getModel(), motorbike.getModel());
        assertEquals(motorbikeDto.getYear(), motorbike.getYear());
        assertEquals(motorbikeDto.getMileage(), motorbike.getMileage());

        Motorbike motorbikeFound = motorbikeRepository.findById(motorbike.getIdMotorbike()).get();
        assertEquals(motorbikeDto.getMake(), motorbikeFound.getMake());
        assertEquals(motorbikeDto.getModel(), motorbikeFound.getModel());
        assertEquals(motorbikeDto.getYear(), motorbikeFound.getYear());
        assertEquals(motorbikeDto.getMileage(), motorbikeFound.getMileage());

        assertEquals(motorbike.getIdMotorbike(), motorbikeFound.getIdMotorbike());
    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutModel() throws Exception {
        final MotorbikeDto motorbikeDto = new MotorbikeDto(null, "R 1250 GS Adventure", 2020, 0);

        ResponseEntity<?> responseEntity = motorbikeController.createMotorbike(motorbikeDto);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(responseEntity.getBody());

    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutMake() throws Exception {
        final MotorbikeDto motorbikeDto = new MotorbikeDto("BMW", null, 2020, 0);

        ResponseEntity<?> responseEntity = motorbikeController.createMotorbike(motorbikeDto);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(responseEntity.getBody());
    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutBadYear1() throws Exception {
        final MotorbikeDto motorbikeDto = new MotorbikeDto("BMW", "R 1250 GS Adventure", 1899, 0);

        ResponseEntity<?> responseEntity = motorbikeController.createMotorbike(motorbikeDto);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(responseEntity.getBody());
    }

    @Test
    void shouldReturn400WhenCreateNewMotorbikeWithoutBadYear2() throws Exception {
        int nextYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
        final MotorbikeDto motorbikeDto = new MotorbikeDto("BMW", "R 1250 GS Adventure", nextYear, 0);

        ResponseEntity<?> responseEntity = motorbikeController.createMotorbike(motorbikeDto);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNull(responseEntity.getBody());
    }

    @Test
    void shouldFinaAndUpdateMotorbike() throws Exception {
        Motorbike motorbike = motorbikeController.getMotorbike(motorbikes.get(0).getIdMotorbike()).getBody();

        assertEquals(motorbikes.get(0).getMake(), motorbike.getMake());
        assertEquals(motorbikes.get(0).getModel(), motorbike.getModel());
        assertEquals(motorbikes.get(0).getYear(), motorbike.getYear());
        assertEquals(motorbikes.get(0).getIdMotorbike(), motorbike.getIdMotorbike());
        assertEquals(motorbikes.get(0).getMileage(), motorbike.getMileage());

        MotorbikeDto newMotorbike = new MotorbikeDto("JAWA", "250", 1960, 35000);

        ResponseEntity<Motorbike> responseEntity = motorbikeController.editMotorbike(newMotorbike, motorbikes.get(0).getIdMotorbike());

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        assertEquals(newMotorbike.getMake(), responseEntity.getBody().getMake());
        assertEquals(newMotorbike.getModel(), responseEntity.getBody().getModel());

    }

    @Test
    void shouldDeleteAndTryFoundMotorbike() throws Exception {
        ResponseEntity<?> responseDeleteEntity = motorbikeController.removeMotorbike(motorbikes.get(0).getIdMotorbike());

        assertEquals(responseDeleteEntity.getStatusCode(), HttpStatus.NO_CONTENT);

        ResponseEntity<?> responseFoundEntity = motorbikeController.getMotorbike(motorbikes.get(0).getIdMotorbike());

        assertEquals(responseFoundEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
