package bfg.backend.repository.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private Integer current_day;
    private Integer days_before_delivery;
    private Boolean live;

    public User(Long id, String name, String email, String password, Integer current_day, Integer days_before_delivery, Boolean live) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        setPassword(password);
        this.current_day = current_day;
        this.days_before_delivery = days_before_delivery;
        this.live = live;
    }

    public User() {}

    // Метод для хэширования пароля перед сохранением
    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    // Проверка пароля
    public boolean checkPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, this.password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   /* public String getPassword() {
        return password;
    }*/

    public Integer getCurrent_day() {
        return current_day;
    }

    public void setCurrent_day(Integer current_day) {
        this.current_day = current_day;
    }

    public Integer getDays_before_delivery() {
        return days_before_delivery;
    }

    public void setDays_before_delivery(Integer days_before_delivery) {
        this.days_before_delivery = days_before_delivery;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
