package com.example.spgtu.service;

import com.example.spgtu.command.Command;
import com.example.spgtu.command.CommandFactory;
import com.example.spgtu.dao.entities.standart.Button;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.repositories.ButtonRepo;
import com.example.spgtu.dao.repositories.MessageRepo;
import com.example.spgtu.dao.repositories.TelegramBotRepositoryProvider;
import com.example.spgtu.dao.repositories.UserRepo;
import com.example.spgtu.exceptions.CommandNotFoundException;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.UpdateUtil;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class CommandService {

    private long chatId;
    private ButtonRepo buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
    private MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
    private UserRepo userRepo = TelegramBotRepositoryProvider.getUserRepo();

    public Optional<Command> getCommand(Update update) throws CommandNotFoundException {
        chatId = UpdateUtil.getChatId(update);
        Message updateMessage = update.getMessage();
        String inputtedText;
        if (update.hasCallbackQuery()){
            inputtedText = update.getCallbackQuery().getData().split(Const.SPLIT)[0];
            updateMessage = update.getCallbackQuery().getMessage();
            try {
                if (inputtedText != null && inputtedText.equals("238") || inputtedText.equals("239")){ //inputtedText.substring(0,6).equals(Const.ID_MARK)
                    try {
                        int comId = buttonRepo.findByIdAndLangId(Integer.parseInt(inputtedText), 1).getCommandId();
                        return Optional.ofNullable(getCommandById(comId));
                    } catch (Exception e){
                        inputtedText = updateMessage.getText();
                    }
                }
            }catch (Exception e){}
        }
        else {
            try {
                inputtedText = updateMessage.getText();
            } catch (Exception e){
                throw new CommandNotFoundException(e);
            }
        }

        try {
            return getCommand(buttonRepo.findByName(inputtedText));
        }catch (IncorrectResultSizeDataAccessException | NonUniqueResultException e){
            User user;
            if (userRepo.findByChatId(chatId) != null){
                user = userRepo.findByChatId(chatId);
            }else user = new User();
            return getCommand(buttonRepo.findByName(inputtedText));
        }
    }

    public Optional<Command> getCommand(Optional<Button> button) throws CommandNotFoundException{
        return button.map(Button::getCommandId).map(integer -> {
            return Optional.ofNullable(CommandFactory.getCommand(integer)).map(command -> {
                command.setId(integer);

//                command.setMessageId(button.map(Button::getMessageId).orElse(0));
                return command;
            });
        }).orElseThrow(() -> new CommandNotFoundException());
    }

    private Command getCommandById(int id) {
        return CommandFactory.getCommand(id);
    }

    protected Language getLanguage(){
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }
}