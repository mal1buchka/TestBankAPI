package kg.test.test_project_for_bank.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import java.time.ZonedDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Name of the user cannot be empty, please try again")
    @Column(name = "user_name", nullable = false)
    private String name;

    @NotBlank(message = "Email of the user cannot be empty, please try again")
    @Email(message = "Not valid email, please try another one")
    @NaturalId
    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @CreationTimestamp
    @Column (name = "user_date_of_registration", updatable = false)
    private ZonedDateTime dateOfRegistration;

}
