package com.paperized.worldscrape.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "uc_user_username", columnNames = "username"),
  @UniqueConstraint(name = "uc_user_email", columnNames = "email")})
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false, length = 16)
  private String username;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  private boolean isEnabled;
  @Column(nullable = false)
  private Timestamp creationTime;

  private String firstName;
  private String lastName;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    foreignKey = @ForeignKey(name = "fk_userroles_user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
    inverseForeignKey = @ForeignKey(name = "fk_userroles_role_id"))
  private List<Role> roles = new ArrayList<>();

  public User(Long id) {
    this.id = id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
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
}


