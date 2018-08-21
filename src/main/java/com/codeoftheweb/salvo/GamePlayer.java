package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> fleet = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvo = new HashSet<>();

    private Date time;

    public GamePlayer() { }

    public GamePlayer(Player player, Game game, Date time) {
        this.player = player;
        this.game = game;
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getId() {
        return id;
    }

    public void addShip(Ship ship){
        this.fleet.add(ship);
        ship.setGamePlayer(this);
    }

    public Set<Ship> getFleet() {
        return fleet;
    }

    public Set<Salvo> getSalvo() {
        return salvo;
    }

    public void setSalvo(Set<Salvo> salvo) {
        this.salvo = salvo;
    }
}
