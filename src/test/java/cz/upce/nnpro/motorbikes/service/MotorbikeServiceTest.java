package cz.upce.nnpro.motorbikes.service;

import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import cz.upce.nnpro.motorbikes.repository.MotorbikeRepository;
import cz.upce.nnpro.motorbikes.service.impl.MotorbikeServiceImpl;
import cz.upce.nnpro.motorbikes.service.MotorbikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MotorbikeServiceTest {

    @Mock
    MotorbikeRepository motorbikeRepository;

    @InjectMocks
    MotorbikeServiceImpl motorbikeService;

    @Test
    public void shouldSavedMotorbikeSuccessFully() {

        final Motorbike motorbike = new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0);
        final MotorbikeDto motorbikeDto = new MotorbikeDto("BMW", "R 1250 GS Adventure", 2020, 0);

        given(motorbikeRepository.save(any(Motorbike.class))).willReturn(motorbike);

        Motorbike expected = motorbikeService.createMotorbike(motorbikeDto);

        assertThat(expected).isNotNull();

        verify(motorbikeRepository).save(any(Motorbike.class));

    }

    @Test
    void shouldReturnFindAll() {
        List<Motorbike> motorbikes = new ArrayList<>();
        motorbikes.add(new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0));
        motorbikes.add(new Motorbike(2, "Kawasaki", "KLE 500", 2007, 27000));
        motorbikes.add(new Motorbike(3, "Yamaha", "XT 660 Z Tenere", 2015,30000));

        given(motorbikeRepository.findAll()).willReturn(motorbikes);

        List<Motorbike> expected = motorbikeService.getAllMotorbikes();

        assertEquals(expected, motorbikes);
    }

    @Test
    void shouldEditmotorbike() {
        final Motorbike motorbike = new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0);
        final MotorbikeDto motorbikeDto = new MotorbikeDto(motorbike.getMake(), motorbike.getModel(), motorbike.getYear(), motorbike.getMileage());

        given(motorbikeRepository.save(motorbike)).willReturn(motorbike);

        final Motorbike expected = motorbikeService.editMotorbike(motorbikeDto, motorbike.getIdMotorbike());

        assertThat(expected).isNotNull();

        verify(motorbikeRepository).save(any(Motorbike.class));
    }

    @Test
    void shouldfindMotorbikeById(){
        final int id = 1;
        final Motorbike motorbike = new Motorbike(1, "BMW", "R 1250 GS Adventure", 2020, 0);

        given(motorbikeRepository.findByIdMotorbike(id)).willReturn(motorbike);

        final Motorbike expected  = motorbikeRepository.findByIdMotorbike(motorbike.getIdMotorbike());

        assertThat(expected).isNotNull();
    }

    @Test
    void shouldBeDelete() {
        final int id = 1;

        motorbikeService.removeMotorbike(id);
        motorbikeService.removeMotorbike(id);

        verify(motorbikeRepository, times(2)).deleteById(id);
    }
}
