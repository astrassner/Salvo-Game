package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Entity //@Entity tells Spring to create a game table for this class
public class Game {

    @Id //@Id says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) //@GeneratedValue tells JPA to get the Id from the DBMS (This happens the first time an instance of Person is saved)
    private long id;
    /*private String firstName;
    private String lastName;*/
    private String gameName;
    private Date gameTime;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> score;

    /*You must define a default (no-argument) constructor for any entity class.
    That's what JPA will call to create new instances.*/

    public Game() { }

    public Game(Date time) {

        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.gameName = date.format(time);
        this.gameTime = time;

    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Date getGameTime() {
        return gameTime;
    }

    public void setGameTime(Date gameTime) {
        this.gameTime = gameTime;
    }

    public long getId() {
        return id;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }

    /*public void setGameTime (){
        this.gameTime = new Date();
    }*/

    /*public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }*/

    public String toString() {
        return gameName;
    }
}

