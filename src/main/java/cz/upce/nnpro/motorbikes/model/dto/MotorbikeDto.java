package cz.upce.nnpro.motorbikes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotorbikeDto {
    private String make;
    private String model;
    private int year;
    private int mileage;
}
