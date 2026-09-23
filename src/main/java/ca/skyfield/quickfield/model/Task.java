package ca.skyfield.quickfield.model;

import ca.skyfield.quickfield.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "tasks"
    )
    private List<Employee> employees;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Customer customer;

}
