package TotalKnockoutChess.Boxing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint(value = "/boxingGame/{player1}/{player2}")
public class BoxingGameSocket {

    private static BoxingGameRepository boxingGameRepository;

    @Autowired
    public void setBoxingGameRepository(BoxingGameRepository boxingGameRepository) {
        this.boxingGameRepository = boxingGameRepository;
    }
}
