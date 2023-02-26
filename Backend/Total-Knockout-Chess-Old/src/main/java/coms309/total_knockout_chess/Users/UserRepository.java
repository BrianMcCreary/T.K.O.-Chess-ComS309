package coms309.total_knockout_chess.Users;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;

@Table
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    void deleteById(int id);

    void save(User user);
}
