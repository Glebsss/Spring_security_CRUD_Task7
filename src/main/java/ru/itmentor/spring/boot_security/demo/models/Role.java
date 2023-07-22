package ru.itmentor.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="Role")
public class Role implements GrantedAuthority {
    @Transient
    @ManyToMany(mappedBy = "roles",fetch=FetchType.EAGER)
    private Set<User> users;
//Параметр fetch=FetchType.EAGER говорит о том, что при загрузке
// владеемого объекта необходимо сразу загрузить и коллекцию владельцев.
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Role should not be Empty!")
    @Size(min = 2, max =30, message ="Name should be between 2 and 30 characters. ")
    @Column(name="roleName")
    private String roleName;

    public Role() {
    }

    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return  roleName;
    }

    @Override
    public String getAuthority() {
        return getRoleName();
    }
}
