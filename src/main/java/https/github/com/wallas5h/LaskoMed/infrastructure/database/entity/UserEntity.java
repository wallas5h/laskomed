package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @NotBlank(message = "Username cannot be blank")
  @Size(min = 3, message = "Username must have min 3 characters")
  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Invalid email format. Email should have @")
  @Column(name = "email")
  private String email;

  @Column(name = "enabled")
  private Boolean enabled;

  @Column(name = "confirmed")
  private Boolean confirmed;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinTable(
      name = "app_user_role",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")}
  )
  private Set<RoleEntity> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
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
