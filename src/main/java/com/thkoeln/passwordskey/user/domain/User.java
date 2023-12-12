package com.thkoeln.passwordskey.user.domain;

import com.thkoeln.passwordskey.domainprimitives.EMail;
import com.thkoeln.passwordskey.securityquestion.domain.Question;
import com.thkoeln.passwordskey.token.Token;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User implements UserDetails {
    @Id
    private String id;
    private String fullName;
    @Email
    private EMail email;
    private String password;
    private String resetPasswordCode;
    private Role role;
    private Date createdTime;
    @DBRef
    private List<Token> tokens;
    @DBRef
    private Question question;
    private String questionAnswer;
    private String imageId;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email.getAddress();
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
        return this.enabled && email.isVerified();
    }

}
