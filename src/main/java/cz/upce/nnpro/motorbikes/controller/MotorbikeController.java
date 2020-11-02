package cz.upce.nnpro.motorbikes.controller;

import cz.upce.nnpro.motorbikes.model.Motorbike;
import cz.upce.nnpro.motorbikes.model.dto.MotorbikeDto;
import cz.upce.nnpro.motorbikes.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@RestController
public class MotorbikeController {
    @Autowired
    private MotorbikeService motorbikeService;

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/motorbike")
    public ResponseEntity<Motorbike> createMotorbike(@RequestBody MotorbikeDto motorbikeDto) {
        if(motorbikeDto.getMake() == null || motorbikeDto.getModel() == null
                || motorbikeDto.getYear() < 1900 || motorbikeDto.getYear() > Calendar.getInstance().get(Calendar.YEAR))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(motorbikeService.createMotorbike(motorbikeDto));
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/motorbike/{idMotorbike}")
    public ResponseEntity<Motorbike> editMotorbike(@RequestBody MotorbikeDto motorbikeDto, @PathVariable int idMotorbike) {
        if(motorbikeDto.getMake() == null || motorbikeDto.getModel() == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(motorbikeService.editMotorbike(motorbikeDto, idMotorbike));
    }

    @DeleteMapping("/{idMotorbike}")
    public ResponseEntity<?> removeMotorbike(@PathVariable int idMotorbike) {
        try {
            motorbikeService.removeMotorbike(idMotorbike);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/motorbike/{idMotorbike}")
    public ResponseEntity<Motorbike> getMotorbike(@PathVariable int idMotorbike){
        Motorbike motorbike = motorbikeService.getMotorbike(idMotorbike);
        if(motorbike != null)
            return ResponseEntity.ok(motorbike);
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/motorbikes")
    public ResponseEntity<List<Motorbike>> getMotorbikes(){
        return ResponseEntity.ok(motorbikeService.getAllMotorbikes());
    }
}
