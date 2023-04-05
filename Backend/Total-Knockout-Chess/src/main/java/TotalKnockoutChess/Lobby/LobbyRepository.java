package TotalKnockoutChess.Lobby;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Table;

@Table
public interface LobbyRepository extends JpaRepository<Lobby, Long>{
//    Lobby getById(Long id);
    void deleteById(Long id);

    Lobby getByCode(Long code);
}
