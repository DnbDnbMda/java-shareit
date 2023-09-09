package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

//мдау@Getter
//мдау@Setter
//мдау@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
//мдау@ToString
@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
