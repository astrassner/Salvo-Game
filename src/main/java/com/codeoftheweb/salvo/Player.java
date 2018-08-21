package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity //@Entity tells Spring to create a person table for this class
public class Player {

    @Id //@Id says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) //@GeneratedValue tells JPA to get the Id from the DBMS (This happens the first time an instance of Person is saved)
    private long id;

    private String userName;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> score;

    /*You must define a default (no-argument) constructor for any entity class.
    That's what JPA will call to create new instances.*/

    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Score getScore(Game game){
        return score.stream()
                    .filter(oneScore -> oneScore.getGame().equals(game))
                    .findFirst().orElse(null);
    }

    public Set<Score> getScores() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String toString() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
