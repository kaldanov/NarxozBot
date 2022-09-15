package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.service.ReportGenerator;
import com.example.spgtu.util.DateKeyboard;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;

public class id024_Reports extends Command {
    DateKeyboard dateKeyboard = new DateKeyboard();
    int buttonId = 111;
    Date start;
    Date end;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMessage();
                if (isButton(109))
                    buttonId = 109;
                else if (isButton(110))
                    buttonId = 110;
                else if (isButton(111))
                    buttonId = 111;
                else if (isButton(112))
                    buttonId = 112;
                else if(isButton(113))
                    buttonId = 113;
                else if(isButton(114))
                    buttonId = 114;
                else if(isButton(115))
                    buttonId = 115;
                sendMessageWithKeyboard(1300, dateKeyboard.getWeekCalendarKeyboard());
                waitingType = WaitingType.DATE_START;
                return COMEBACK;
            case DATE_START:
                if (hasCallbackQuery() && dateKeyboard.isNext(updateMessageText)){
                    editMessageWithKeyboard(getText(1300),updateMessageId, dateKeyboard.getWeekCalendarKeyboard());
                }
                else if (hasCallbackQuery() && dateKeyboard.getDateDate(updateMessageText) != null){
                    deleteUpdateMessage();
                    start = dateKeyboard.getDateDate(updateMessageText);
                    start.setHours(0);
                    start.setMinutes(0);
                    start.setSeconds(0);
                    sendMessageWithKeyboard(1301, dateKeyboard.getWeekCalendarKeyboard());
                    waitingType = WaitingType.DATE_END;
                }
                else deleteUpdateMessage();
                return COMEBACK;
            case DATE_END:
                if (hasCallbackQuery() && dateKeyboard.isNext(updateMessageText)){
                    editMessageWithKeyboard(getText(1301),updateMessageId, dateKeyboard.getWeekCalendarKeyboard());
                }
                else if (hasCallbackQuery() && dateKeyboard.getDateDate(updateMessageText) != null){
                    deleteUpdateMessage();
                    end = dateKeyboard.getDateDate(updateMessageText);
                    end.setHours(23);
                    end.setMinutes(59);
                    end.setSeconds(59);
                    int del = sendMessage(138);
                    sendReport(del);
                }
                else deleteUpdateMessage();
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendReport(int del) throws TelegramApiException {
        ReportGenerator reportGenerator = new ReportGenerator();

        if (buttonId == 109)
            reportGenerator.onay(chatId, bot, start, end ,del);
        else if (buttonId == 110)
            reportGenerator.dorm(chatId, bot, start, end ,del);
        else if (buttonId == 111)
            reportGenerator.sendPreparationCourses(chatId, bot, start, end ,del);
        else if (buttonId == 112)
            reportGenerator.sendPreparationCoursesWithout(chatId, bot, start, end ,del);
        else if (buttonId == 113)
            reportGenerator.sendReference(chatId, bot, start, end ,del);
        else if (buttonId == 114)
            reportGenerator.sendReferenceStudent(chatId, bot, start, end ,del);
        else if (buttonId == 115)
            reportGenerator.lossDocs(chatId, bot, start, end ,del);

    }
}
