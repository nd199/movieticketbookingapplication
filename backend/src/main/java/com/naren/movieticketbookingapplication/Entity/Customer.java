package com.naren.movieticketbookingapplication.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Customer", uniqueConstraints = {
        @UniqueConstraint(name = "email_id_unique",
                columnNames = "email"),
        @UniqueConstraint(name = "phone_number_unique",
                columnNames = "phoneNumber")
})
@Getter
@Setter
@NoArgsConstructor
public class Customer implements UserDetails {
    @Id
    @SequenceGenerator(name = "customer_id",
            sequenceName = "customer_id",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "customer_id")
    private Long customer_id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "email", columnDefinition = "TEXT", nullable = false)
    private String email;

    @Column(name = "password", columnDefinition = "TEXT", nullable = false, length = 8)
    private String password;

    @Column(name = "phoneNumber", nullable = false)
    private Long phoneNumber;

    public Customer(Long customer_id, String name, String email, String password, Long phoneNumber) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public Customer(String name, String email, String password, Long phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customer_id, customer.customer_id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(password, customer.password) && Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer_id, name, email, password, phoneNumber);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
