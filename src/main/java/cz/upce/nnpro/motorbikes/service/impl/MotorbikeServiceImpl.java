package cz.upce.nnpro.motorbikes.service.impl;

import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import cz.upce.nnpro.motorbikes.repository.MotorbikeRepository;
import cz.upce.nnpro.motorbikes.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "motorbikeService")
public class MotorbikeServiceImpl implements MotorbikeService {
    @Autowired
    private MotorbikeRepository motorbikeRepository;

    @Override
    public Motorbike createMotorbike(MotorbikeDto motorbikeDto) {

        Motorbike motorbike = new Motorbike();
        motorbike.setMake(motorbikeDto.getMake());
        motorbike.setMileage(motorbikeDto.getMileage());
        motorbike.setModel(motorbikeDto.getModel());
        motorbike.setYear(motorbikeDto.getYear());
        return motorbikeRepository.save(motorbike);
    }

    @Override
    public Motorbike editMotorbike(MotorbikeDto motorbikeDto, int idMotorbike) {

        Motorbike motorbike = new Motorbike();
        motorbike.setIdMotorbike(idMotorbike);
        motorbike.setMake(motorbikeDto.getMake());
        motorbike.setMileage(motorbikeDto.getMileage());
        motorbike.setModel(motorbikeDto.getModel());
        motorbike.setYear(motorbikeDto.getYear());

        return motorbikeRepository.save(motorbike);
    }

    @Override
    public void removeMotorbike(int idMotorbike) {
        motorbikeRepository.deleteById(idMotorbike);
    }

    @Override
    public List<Motorbike> getAllMotorbikes() {
        return motorbikeRepository.findAll();
    }

    @Override
    public Motorbike getMotorbike(int idMotorbike) {
        return motorbikeRepository.findByIdMotorbike(idMotorbike);
    }
}
