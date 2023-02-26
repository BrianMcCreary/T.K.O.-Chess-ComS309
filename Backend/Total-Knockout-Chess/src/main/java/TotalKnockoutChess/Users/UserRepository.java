package TotalKnockoutChess.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Table;

@Table
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    void deleteById(int id);
}
