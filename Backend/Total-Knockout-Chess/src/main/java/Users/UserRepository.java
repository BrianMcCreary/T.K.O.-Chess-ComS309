package Users;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Table
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    void deleteById(int id);

    void save(User user);
}
