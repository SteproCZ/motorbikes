package cz.upce.nnpro.motorbikes.repository;

import cz.upce.nnpro.motorbikes.model.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorbikeRepository extends JpaRepository<Motorbike, Integer> {
    Motorbike findByIdMotorbike(int idMotorbike);
}
