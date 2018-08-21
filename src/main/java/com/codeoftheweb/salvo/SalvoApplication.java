package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	/*You use @Bean annotation to mark a method that returns an instance of a Java bean. When Spring sees this,
	it will run the method and save the bean instance for later use.*/

	@Bean
	public CommandLineRunner initData(PlayerRepository repository,
									  GameRepository repositoryGame,
									  GamePlayerRepository repositoryGamePlayer,
									  ShipRepository repositoryShip,
									  SalvoRepository repositorySalvo,
									  ScoreRepository repositoryScore) {
		return (args) -> {
			// save a couple of customers
			Player p1 = new Player("hulk@avengers.com");
			Player p2 = new Player("thor@avengers.com");
			Player p3 = new Player("ironman@avengers.com");
			Player p4 = new Player("thanos@titan.com");
			Player p5 = new Player("loki@jotunheim.com");

			repository.save(p1);
			repository.save(p2);
			repository.save(p3);
			repository.save(p4);
			repository.save(p5);

			Date date = new Date();

			Game g1 = new Game(date);
			Game g2 = new Game(date.from(date.toInstant().plusSeconds(3600)));
			Game g3 = new Game(date.from(date.toInstant().plusSeconds(7200)));

			repositoryGame.save(g1);
			repositoryGame.save(g2);
			repositoryGame.save(g3);

			GamePlayer gp1 = new GamePlayer(p1, g1, date);
			GamePlayer gp2 = new GamePlayer(p2, g1, date);
			GamePlayer gp3 = new GamePlayer(p5, g2, date);
			GamePlayer gp4 = new GamePlayer(p4, g2, date);
			GamePlayer gp5 = new GamePlayer(p3, g3, date);
			GamePlayer gp6 = new GamePlayer(p1, g3, date);

			repositoryGamePlayer.save(gp1);
			repositoryGamePlayer.save(gp2);
			repositoryGamePlayer.save(gp3);
			repositoryGamePlayer.save(gp4);
			repositoryGamePlayer.save(gp5);
			repositoryGamePlayer.save(gp6);

			List<String> locations = Arrays.asList("H1", "H2", "H3", "H4");

			List<String> l2 = Arrays.asList("B2", "C2", "D2");

			List<String> l3 = Arrays.asList("D4", "D5");

			List<String> l8 = Arrays.asList("G5", "H5", "I5", "J5");

			Ship ship1 = new Ship("Battleship", locations);
			Ship ship2 = new Ship("Destroyer", l2);
			Ship ship3 = new Ship("Patrol Boat", l3);
			Ship ship4 = new Ship("Battleship", l8);

			gp1.addShip(ship1);
			gp2.addShip(ship2);
			gp1.addShip(ship3);
			gp2.addShip(ship4);

			repositoryShip.save(ship1);
			repositoryShip.save(ship2);
			repositoryShip.save(ship3);
			repositoryShip.save(ship4);

			List<String> l4 = Arrays.asList("D4", "H5", "A1", "J8", "E10");
			List<String> l5 = Arrays.asList("D3", "H2", "B1", "J5", "A10");
			List<String> l6 = Arrays.asList("D1", "H2", "B2", "J5", "A10");
			List<String> l7 = Arrays.asList("A3", "H5", "C6", "E5", "I3");

			Salvo s1 = new Salvo(1, l4, gp1);
			Salvo s2 = new Salvo(2, l5, gp1);
			Salvo s3 = new Salvo(1, l6, gp2);
			Salvo s4 = new Salvo(2, l7, gp2);

			repositorySalvo.save(s1);
			repositorySalvo.save(s2);
			repositorySalvo.save(s3);
			repositorySalvo.save(s4);

			Score score1 = new Score(p1, g1, date, 1.0);
			Score score2 = new Score(p2, g1, date, 0.0);
			Score score3 = new Score(p5, g2, date, 0.5);
			Score score4 = new Score(p3, g3, date, 0.0);
			Score score5 = new Score(p4, g2, date, 0.5);
			Score score6 = new Score(p1, g3, date, 1.0);

			repositoryScore.save(score1);
			repositoryScore.save(score2);
			repositoryScore.save(score3);
			repositoryScore.save(score4);
			repositoryScore.save(score5);
			repositoryScore.save(score6);

		};
	}
}
