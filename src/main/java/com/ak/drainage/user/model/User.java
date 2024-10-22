package com.ak.drainage.user.model;


import com.ak.drainage.user.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable , UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fname;
    private String lname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private List<String> authorities;
    private boolean isActive;
    private boolean isEnabled;
    private boolean isNotLocked;
    private String verificationCode;
    private boolean isVerified;
    private Date joinDate;
    private Date lastLoginDate;
    private Date createDate;
    private Date lastUpdateDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
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
    public boolean isEnabled(boolean isEnabled) {
        return this.isEnabled = isEnabled;
    }
    public boolean isVerified(boolean isVerified) {
        return this.isVerified = isVerified;
    }


}
