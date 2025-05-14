package com.devtale.ecommerceplatform2.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @NaturalId
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * The mappedBy = "user" part refers to the inverse side of the bidirectional @OneToOne relationship between User and Cart.
     * Where Does user Come From?
     * The user field is defined inside the Cart entity.
     *
     * - The Cart entity has a foreign key (user_id) pointing to the User entity.
     * - The user field in Cart represents the owning side of the relationship.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private Cart cart;

    /**
     * How mappedBy = "user" Works in User
     * @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     * private Cart cart;
     * - mappedBy = "user" tells Hibernate that Cart owns the relationship.
     * - No extra foreign key is created in the User table.
     * - CascadeType.ALL ensures that changes to User affect Cart (e.g., deleting a user deletes their cart).
     * - orphanRemoval = true ensures that if a User is removed, their Cart is also
     */
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();


    /**
     * @ManyToMany(fetch = FetchType.EAGER, cascade = {...})
     * - @ManyToMany → A user can have multiple roles, and a role can belong to multiple users.
     * - fetch = FetchType.EAGER → Loads roles immediately when fetching a user.
     * - cascade = {CascadeType.DETACH, MERGE, PERSIST, REFRESH} → Ensures changes in User affect Role (but avoids REMOVE to prevent unintended deletions).
     * 2. @JoinTable(name = "user_roles", ...)
     * - Creates a join table (user_roles) to store user-role relationships.
     * 3. joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
     * - Defines the foreign key (user_id) in user_roles that references User.id.
     * 4. inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
     * - Defines the foreign key (role_id) in user_roles that references Role.id.
     * 5. private Set<Role> roles = new HashSet<>();
     * - Stores the roles assigned to a user in a Set<Role>.
     */
    @ManyToMany(fetch = FetchType.EAGER,cascade =
            {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();
}
