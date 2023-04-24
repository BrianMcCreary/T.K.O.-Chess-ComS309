package TotalKnockoutChess.Lobby;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Table;

@Table
public interface LobbyRepository extends JpaRepository<Lobby, Long>{
    Lobby findById(int id);

    Lobby getByCode(Long code);
}
