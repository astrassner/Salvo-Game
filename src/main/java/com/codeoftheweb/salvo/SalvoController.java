package com.codeoftheweb.salvo;

import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

//A controller in Spring is a class with methods to run when requests with specific URL patterns are received

//the RestController returns data using JSON

@RestController
@RequestMapping("/api") //makes sure that all the URLs this controller looks for will have to start with /api
public class SalvoController{

    @Autowired //build connections
    private GameRepository gameRepository;

    @Autowired //build connections
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> getAll = new LinkedHashMap<>();

        if(authentication != null){
            getAll.put("player", getLoggedPlayer(authentication));
        }

        getAll.put("games", gameRepository.findAll().stream()
                .map(game -> getGameDTO(game))
                .collect(toList()));

        return getAll;



        /*return gameRepository.findAll().stream()
                                        .map(game -> getGameDTO(game))
                                        .collect(toList());*/
    }

    public Map<String, Object> getLoggedPlayer (Authentication authentication){
        Map<String, Object> loggedPlayer = new LinkedHashMap<>();
        Player authPlayer = playerRepository.findByUserName(authentication.getName());

        loggedPlayer.put("id", authPlayer.getId());
        loggedPlayer.put("name", authPlayer.getUserName());

        return loggedPlayer;
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getInfo(@PathVariable long nn) {
        Map<String, Object> infoGamePlayer = new LinkedHashMap<>();
        GamePlayer gp = gamePlayerRepository.findOne(nn);

        infoGamePlayer.put("id", gp.getGame().getId());
        infoGamePlayer.put("created", gp.getGame().getGameName());
        infoGamePlayer.put("gamePlayers", gp.getGame().getGamePlayers().stream()
                                                                        .map(gamePlayer -> getGamePlayerDTO(gamePlayer))
                                                                        .collect(toList()));
        infoGamePlayer.put("ships", gp.getFleet().stream()
                                                    .map(ship -> getShipsDTO(ship))
                                                    .collect(toList()));

        infoGamePlayer.put("salvos", getPlayerOfSalvoDTO(gp));

        return infoGamePlayer;
    }

    @RequestMapping("/leader_board")
    public Map<String, Object> getScoreInfos(){
        Map<String, Object> scoreMap = new HashMap<>();
        List<GamePlayer> gamePlayers = gamePlayerRepository.findAll();

        for(GamePlayer gp : gamePlayers){
            if(!scoreMap.containsKey(gp.getPlayer().getUserName())) {
                Map<String, Object> scores = new LinkedHashMap<>();

                scores.put("wins", gp.getPlayer().getScores().stream().filter(gameScore -> gameScore.getScore() == 1).count());

                scores.put("draws", gp.getPlayer().getScores().stream().filter(gameScore -> gameScore.getScore() == 0.5).count());

                scores.put("looses", gp.getPlayer().getScores().stream().filter(gameScore -> gameScore.getScore() == 0).count());

                scores.put("total", gp.getPlayer().getScores().stream().mapToDouble(gameScore -> gameScore.getScore()).sum());

                scoreMap.put(gp.getPlayer().getUserName(), scores);
            }
        }

        return scoreMap;
    }

    @RequestMapping(value ="/players", method= RequestMethod.POST)
    public ResponseEntity<String> createPlayer(String userName, String password){

        Player player = playerRepository.findByUserName(userName);

        if(userName.length() <= 6 || userName.indexOf("@") == -1){
            return new ResponseEntity<>("You have to type your email", HttpStatus.CONFLICT);

        }else if(password.length() < 3) {
            return new ResponseEntity<>("Your password is not long enough", HttpStatus.CONFLICT);

        }else if(player == null){
            Player newPlayer = new Player(userName, password);

            playerRepository.save(newPlayer);

            return new ResponseEntity<>("Player greated", HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("Username exist", HttpStatus.FORBIDDEN);
        }

    }



/*    public List<Object> getPlayerOfSalvoDTO(GamePlayer gamePlayer){

        Set<Salvo> salvoes = gamePlayer.getSalvo();
        List<Salvo> allSalvoes = new ArrayList<>();

        for (Salvo salvo : allSalvoes) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("playerId", gamePlayer.getPlayer().getId());
            dto.put("turn", salvo.getTurn());
            dto.put("locations", salvo.getLocations());
            allSalvoes.add(dto);
        }

        return allSalvoes;
    }*/

    public List<Object> getPlayerOfSalvoDTO(GamePlayer gamePlayer){

        Set<Salvo> oneSet = new HashSet<>();
        gamePlayer.getGame().getGamePlayers().stream()
                .forEach(gamePlayer1 -> gamePlayer1.getSalvo().stream().forEach(salvo -> oneSet.add(salvo)));

        return oneSet.stream().map(salvo -> salvoDTO(salvo)).collect(toList());

    }

    public Map<String, Object> salvoDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("playerId", salvo.getGamePlayer().getPlayer().getId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    public Map<String, Object> getGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getGameName());
        dto.put("gamePlayers", game.getGamePlayers().stream()
                                                    .map(gamePlayer -> getGamePlayerDTO(gamePlayer))
                                                    .collect(toList()));
        if(!game.getScore().isEmpty()){
            dto.put("scores", game.getScore().stream()
                                            .map(oneScore -> makeScoreDTO(oneScore))
                                            .collect(Collectors.toSet()));
        }

        return dto;
    }

    private Map<String, Number> makeScoreDTO(Score score){
        Map<String, Number> dto = new HashMap<>();

        dto.put("playerID", score.getPlayer().getId());
        dto.put("score", score.getScore());

        return dto;
    }



    public Map<String, Object> getGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", getPlayerDTO(gamePlayer.getPlayer()));

        return dto;
    }

    public Map<String, Object> getPlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }

    public Map<String, Object> getShipsDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", ship.getShipType());
        dto.put("locations", ship.getLocations());
        return dto;
    }


}

        /*Spring uses a library called Jackson to convert the Java data to JSON.
          Jackson will create a JSON array to hold the list, and inside the array will be
          JSON key-value objects to represent each contact

        Spring MVC and Spring REST share many common parts. They differ primarily in what the controller returns

        Because there can be multiple controllers, its easy to have an MVC controller serve HTML and a REST
        controller serve JSON in the same web application

        Server-side templates and a template engine are not necessary if you have a REST controller returning JSON.
          Instead, you can serve static HTML, CSS, and JavaScript, and have the JavaScript on the client side
          translate JSON data into HTML content

        At the front of every web service is a dispatcher. A dispatcher is code that examines the URL sent by a
          client and decides what method of what class to pass the URL to


          */