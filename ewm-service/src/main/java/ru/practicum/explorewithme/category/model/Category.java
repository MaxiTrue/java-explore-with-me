package ru.practicum.explorewithme.category.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "categories")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
