package data.repositories;

import data.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public User findByFullName(String fullName) {
        return find("fullName", fullName).firstResult();
    }

    public List<User> listAllUsers(int pageIndex, int pageSize) {
        return findAll().page(pageIndex, pageSize).list();
    }

    public Long countTotalUsers() {
        return count();
    }

    public boolean deleteByEmail(String email) {
        long count = delete("email", email);
        return count > 0;
    }
}
