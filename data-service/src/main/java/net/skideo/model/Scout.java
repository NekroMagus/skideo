package net.skideo.model;

import net.skideo.dto.RegDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scout")
public class Scout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String region;
    @ManyToOne
    private Club club;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> favoriteUsers;

    public Scout(RegDto regDto) {
        this.login=regDto.getLogin();
        this.password=regDto.getPassword();
        this.name=regDto.getName();
        this.surname=regDto.getSurname();
    }

}
