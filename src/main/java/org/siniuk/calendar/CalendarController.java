package org.siniuk.calendar;

import lombok.AllArgsConstructor;
import org.siniuk.calendar.utils.DateChoiceText;
import org.siniuk.calendar.utils.Locale;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CalendarController {

    private final Locale locale;
    private final TelegramLongPollingBot bot;
    private final int messageId;
    private final String chatId;

    public void startCalendar() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = generateMonthCalendar(LocalDate.now());

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        keyboardMarkup.setKeyboard(keyboardRowList);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void control(CallbackQuery callbackQuery) {
        // Handle callback data
        String callbackData = callbackQuery.getData();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        clearPreviousMarkup();

        // Create a message object
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);

        if (callbackData.contains("year_")) {
            LocalDate date = LocalDate.parse(callbackData.replaceAll("year_", ""));
            keyboardRowList.addAll(generateYearCalendar(date));
        } else if (callbackData.contains("month_")){
            LocalDate date = LocalDate.parse(callbackData.replaceAll("month_", ""));
            keyboardRowList.addAll(generateMonthCalendar(date));
        } else {
            if (locale.equals(Locale.RU)) {
                message.setText(DateChoiceText.RU.getText() + callbackData.replaceAll("day_", ""));
            } else {
                message.setText(DateChoiceText.EN.getText() + callbackData.replaceAll("day_", ""));
            }
        }
        keyboardMarkup.setKeyboard(keyboardRowList);
        message.setReplyMarkup(keyboardMarkup);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<List<InlineKeyboardButton>> generateMonthCalendar(LocalDate date) {
        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();
        calendar.add(InlineCalendar.createYearAndMonthRow(date, locale));
        calendar.add(InlineCalendar.createDayOfWeekRow(locale));
        calendar.addAll(InlineCalendar.createDayOfMonthKeyboard(date));
        return calendar;
    }

    private List<List<InlineKeyboardButton>> generateYearCalendar(LocalDate date) {
        return InlineCalendar.createMonthKeyboard(date, locale);
    }

    private void clearPreviousMarkup() {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(null);

        try {
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
