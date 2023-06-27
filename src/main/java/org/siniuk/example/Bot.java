package org.siniuk.example;

import org.siniuk.calendar.CalendarController;
import org.siniuk.calendar.InlineCalendar;
import org.siniuk.calendar.utils.Locale;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    CalendarController calendarController = new CalendarController(Locale.RU, this);

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            // Handle callback data
            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (callbackQuery.getData().contains(InlineCalendar.CONTROL_ALIAS)) {
                calendarController.control(callbackQuery);
            } else if ((callbackQuery.getData().contains(InlineCalendar.DATE_ALIAS))) {
                String result = calendarController.chosenDate(callbackQuery);
                System.out.println("User date is: " + result);
            }
        } else if ((update.hasMessage())) {
            Message inMess = update.getMessage();
            String chatId = inMess.getChatId().toString();
            String inMessText = inMess.getText();
            if (inMessText.equals("/start")) {
                calendarController.startCalendar(chatId);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
