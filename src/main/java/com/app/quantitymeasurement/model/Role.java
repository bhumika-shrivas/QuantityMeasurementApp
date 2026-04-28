package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")

/**
 * Role entity represents a security/authorization role assigned to users.
 *
 * This JPA entity maps to the "roles" table and stores a small set of
 * predefined role names (enumerated by RoleName). Application code (for
 * example security configuration, user management or authorization checks)
 * references Role instances to determine permissions. Roles are linked to
 * users via a many-to-many relationship declared on the User side.
 */
public class Role {
	
	// Primary key for the role record
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	// The role name stored as a string in the database (e.g. ROLE_USER)
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleName name;
	
	// Enumerated list of allowed role names used by the application.
	// Each constant is persisted as its string value (EnumType.STRING).
	public enum RoleName {
		// Default role assigned to regular application users
		ROLE_USER,
		// Elevated role with administrative privileges
		ROLE_ADMIN
	}

}