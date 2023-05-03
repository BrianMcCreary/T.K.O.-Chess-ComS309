package TotalKnockoutChess.Statistics;


import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Table;

@Table
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    UserStats findById(int id);

    void deleteById(int id);
}

