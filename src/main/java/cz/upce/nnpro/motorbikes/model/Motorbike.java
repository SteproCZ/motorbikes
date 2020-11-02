package cz.upce.nnpro.motorbikes.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "motorbike")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Motorbike implements Serializable{
    @Id
    @GeneratedValue
    private int idMotorbike;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int mileage;
}