package com.example.spgtu;

import com.example.spgtu.config.Bot;
import com.example.spgtu.service.JDBC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class SpgtuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpgtuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		Bot bot = new Bot();
		try {
			 if(JDBC.initIt())
				System.out.println("SUCCESSFUL CONNECTING TO WORKDATABASE");
			 else 			System.out.println("FAIL CONNECTING TO WORKDATABASE");

		}catch (Exception e){
			System.out.println("FAIL CONNECTING TO WORKDATABASE");
		}

		try {
			telegramBotsApi.registerBot(bot);
			log.info("Bot was registered: " + bot.getBotUsername());
		} catch (TelegramApiRequestException e) {
			log.error("Error in main class", e);
		}
	}
}
