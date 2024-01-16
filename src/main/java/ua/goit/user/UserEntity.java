package ua.goit.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ua.goit.url.UrlEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<UrlEntity> urls = new ArrayList<>();
}