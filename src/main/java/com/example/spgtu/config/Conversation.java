package com.example.spgtu.config;

import com.example.spgtu.command.Command;
import com.example.spgtu.command.CommandFactory;
import com.example.spgtu.dao.entities.standart.Message;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.repositories.MessageRepo;
import com.example.spgtu.dao.repositories.TelegramBotRepositoryProvider;
import com.example.spgtu.exceptions.CommandNotFoundException;
import com.example.spgtu.service.CommandService;
import com.example.spgtu.service.KeyboardService;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.UpdateUtil;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Optional;

import static org.jboss.logging.NDC.clear;
@Slf4j
public class Conversation {

    private CommandService commandService = new CommandService();
    private Optional<Command> command;
    private Long chatId;

    public void handleUpdate(Update update, DefaultAbsSender bot) throws SQLException, TelegramApiException {

        printUpdate(update);
        chatId = UpdateUtil.getChatId(update);
        checkLang(chatId);
        try {
            command = commandService.getCommand(update);
        } catch (CommandNotFoundException e) {
            if (chatId < 0) {
                return;
            }
            if (update.hasCallbackQuery() && update.getCallbackQuery().getData().split(";").length ==3){
                String data = update.getCallbackQuery().getData();
//                int comId = Integer.parseInt(data.split("\n")[0]);
                int comId = getInt(data.split(";")[0]);
                command = Optional.ofNullable(CommandFactory.getCommand(comId));
            }

            if (command == null) {
                MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
                int messageId = UpdateUtil.getMessageId(update);
                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
                try {
                    bot.execute(deleteMessage);
                }catch (Exception ignored){}

                KeyboardService keyboardService = new KeyboardService();
                Message message = messageRepo.findByIdAndLangId(Const.COMMAND_NOT_FOUND, LanguageService.getLanguage(chatId).getId());
                SendMessage sendMessage = new SendMessage(chatId, message.getName());
                sendMessage.setReplyMarkup(keyboardService.getKeyboard(1 ,chatId)).setParseMode("html");
                bot.execute(sendMessage);

            }
        }

        if (command.isPresent()) {
            if (command.get().isInitNotNormal(update, bot)) {
                clear();
                return;
            }
            boolean commandFinished = command.get().execute();
            if (commandFinished) {
                clear();
            }
        }
    }

    private void checkLang(long chatId) {
        if (LanguageService.getLanguage(chatId) == null) {
            LanguageService.setLanguage(chatId, Language.ru);
        }
    }
    private int getInt(String str){
        try {
            return Integer.parseInt(str);
        }catch (Exception e){
            return -1;
        }
    }

    private void printUpdate(Update update) {
        String dateMessage = "";
//        if (update.hasMessage()) {
//            dateMessage = DateUtil.getDbMmYyyyHhMmSs(new Date((long) update.getMessage().getDate() * 1000));
//        }
//        log.info("New update get {} -> send response {}", dateMessage, DateUtil.getDbMmYyyyHhMmSs(new Date()));
//

        log.info(UpdateUtil.toString(update));
    }

//    public static DefaultAbsSender getBot()
//    {
//        return Main.getBot();
//    }

//    void clear() {
//        command.get().;
//        command = null;
//    }
}

