package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
/**
 * User entity represents application users and their authentication/authorization data.
 *
 * This JPA entity maps to the "users" table and stores identifying information
 * (username, email), credentials (password), external provider details for social
 * login (provider and providerId), and a set of roles used for authorization.
 *
 * The entity also tracks creation and update timestamps. Lifecycle callbacks
 * (@PrePersist / @PreUpdate) populate these timestamps automatically.
 */
public class User {
    
    // Primary key for the user record
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    // Unique username used to identify/login the user
    @Column(nullable = false, unique = true)
    private String username;
    
    // Display/full name for the user (used in UI and responses)
    private String fullName;
    
    // Unique email address used for notifications and login
    @Column(nullable = false, unique = true)
    private String email;
    
    // Hashed password for local authentication (may be null for OAuth users)
    private String password;
    
    // Authentication provider name (e.g., "local", "google", "github")
    @Column(nullable = false)
    private String provider;
    
    // Provider-specific identifier (for users authenticated via external providers)
    private String providerId;
    
    // Whether the user account is enabled (can be used for soft-blocking users)
    private boolean enabled = true;
    
    // Many-to-many relationship linking users to roles; eager fetch so roles
    // are available immediately for security checks. The join table is
    // "user_roles" with foreign keys "user_id" and "role_id".
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    // Creation timestamp (set once when the entity is persisted)
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    // Last update timestamp (updated on each update)
    private LocalDateTime updatedAt;
    
    // Lifecycle callback invoked before first persist: initialize timestamps
    @PrePersist
    public void perPersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Lifecycle callback invoked before update: refresh the updatedAt timestamp
    @PreUpdate
    public void perUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}