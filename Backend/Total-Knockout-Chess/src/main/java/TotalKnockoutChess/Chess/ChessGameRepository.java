package TotalKnockoutChess.Chess;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Table;

@Table
public interface ChessGameRepository extends JpaRepository<ChessGame, Long> {
    ChessGame findById(int id);

    void deleteById(int id);
}
