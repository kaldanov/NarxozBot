package com.example.spgtu.util;

import com.example.spgtu.dao.entities.standart.Message;
import com.example.spgtu.dao.repositories.MessageRepo;
import com.example.spgtu.dao.repositories.TelegramBotRepositoryProvider;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.type.ParseMode;
import lombok.Getter;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Objects;

public class BotUtil {

    @Getter
    private DefaultAbsSender bot;
    //    private static DaoFactory daoFactory = DaoFactory.getFactory();
    private MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();

    public BotUtil(DefaultAbsSender bot) {
        this.bot = bot;
    }
    public int sendDocument( String document,long chatId )   throws TelegramApiException {

        SendDocument sendDocument = new SendDocument()
                .setParseMode("html")
                .setDocument(document)
                .setChatId(chatId);

        return bot.execute(sendDocument).getMessageId();

    }
    public void editMessage(String text, long chatId, int messageId) throws TelegramApiException {
        EditMessageText new_message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(text);
        try {
            bot.execute(new_message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public int sendMessage(SendMessage sendMessage) throws TelegramApiException {
        try {
            return bot.execute(sendMessage).getMessageId();
        } catch (TelegramApiRequestException e) {
            if (e.getApiResponse().contains("Bad Request: can't parse entities")) {
                sendMessage.setParseMode(null);
                sendMessage.setText(sendMessage.getText() + "\nBad tags");
                return bot.execute(sendMessage).getMessageId();
            } else throw e;
        }
    }

//    public int sendMessage(long messageId, long chatId) throws TelegramApiException {
//        return sendMessage(messageId, chatId);
//    }



    public int sendMessage(String text, long chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode("html");
        return sendMessage(sendMessage);
    }

    public int sendMessage(long messageId, long chatId) throws TelegramApiException {
        int result = 0;
        Message message = messageRepo.findByIdAndLangId(messageId,LanguageService.getLanguage(chatId).getId());//daoFactory.getMessageDao().getMessage(messageId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message.getName());
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode(ParseMode.html.name());
        return sendMessage(sendMessage);
    }

    public int sendContact(long chatId, Contact contact) throws TelegramApiException {
        return bot.execute(new SendContact()
                .setChatId(chatId)
                .setFirstName(contact.getFirstName())
                .setLastName(contact.getLastName())
                .setPhoneNumber(contact.getPhoneNumber())
        ).getMessageId();
    }

    public void deleteMessage(long chatId, int messageId) {
        try {
            bot.execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException ignored) {
        }
    }

    public int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard, long chatId) throws TelegramApiException {
        return sendMessageWithKeyboard(text, keyboard, chatId, 0);
    }

    public int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard, long chatId, int replyMessageId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage()
                .setParseMode(ParseMode.html.name())
                .setChatId(chatId)
                .setText(text)
                .setReplyMarkup(keyboard);
        if (replyMessageId != 0) {
            sendMessage.setReplyToMessageId(replyMessageId);
        }
        return sendMessage(sendMessage);
    }


    public boolean hasContactOwner(Update update) {
        return (update.hasMessage() && update.getMessage().hasContact()) &&  Objects.equals(update.getMessage().getFrom().getId(), update.getMessage().getContact().getUserID());
    }
}
