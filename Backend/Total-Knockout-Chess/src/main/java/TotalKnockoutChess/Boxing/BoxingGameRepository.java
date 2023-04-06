package TotalKnockoutChess.Boxing;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Table;

@Table
public interface BoxingGameRepository extends JpaRepository<BoxingGame, Long> {
    BoxingGame findById(int id);

    void deleteById(int id);
}