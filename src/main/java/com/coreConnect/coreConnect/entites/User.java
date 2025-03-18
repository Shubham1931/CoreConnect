package com.coreConnect.coreConnect.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.stream.Collectors;

@Entity(name = "user")
@Table (name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class User implements UserDetails {
      
        @Id
        private String userId;
        @Column(name = "user_name", nullable = false)
        private String name;
        @Column(name = "user_email", nullable = false, unique = true)
        private String email;
        @Column(name = "user_password")
        @Getter(AccessLevel.NONE)
        private String password;   
        private String profilePic;
        @Column(name = "user_about", length = 1000)

        private String about;
        @Column(name = "user_phoneNumber")    
        private String phoneNumber;
        @Getter(AccessLevel.NONE)
        @Setter(AccessLevel.NONE)
        private boolean enabled = false;
        private boolean emailVerified = false;
        private boolean phoneVerified = false;
        private String emailToken;
        private String cloudinaryImagePublicId;
        // self , google , facebook twitter
        @Enumerated(value = EnumType.STRING)
        private Providers provider = Providers.SELF;
        private String providerUserId;
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
        private List<Contact> contacts = new ArrayList<>();
        @ElementCollection(fetch  = FetchType.EAGER)
        private List<String> roleList =  new ArrayList<>();
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            System.out.println("Assigned Authorities: " + roles);
              return roles;
        } 
        @Override
        public String getPassword() {
                         return this.password;
        }
        @Override
        public String getUsername() {
             
               return this.email;
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
        public boolean isEnabled() {
               
                return this.enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
                
        }
}
