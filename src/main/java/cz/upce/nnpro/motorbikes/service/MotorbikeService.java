package cz.upce.nnpro.motorbikes.service;

import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MotorbikeService {
    Motorbike createMotorbike(MotorbikeDto motorbikeDto);

    Motorbike editMotorbike(MotorbikeDto motorbikeDto, int idMotorbike);

    void removeMotorbike(int idMotorbike);

    List<Motorbike> getAllMotorbikes();

    Motorbike getMotorbike(int idMotorbike);
}
