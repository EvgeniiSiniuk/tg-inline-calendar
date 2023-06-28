package org.siniuk.calendar;

import lombok.AllArgsConstructor;
import org.siniuk.calendar.utils.DateChoiceText;
import org.siniuk.calendar.utils.Locale;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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

    public void startCalendar(String chatId, String calendarMessage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = generateMonthCalendar(LocalDate.now());

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        message.setText(calendarMessage);
        keyboardMarkup.setKeyboard(keyboardRowList);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String resolve(CallbackQuery callbackQuery) {
        // Handle callback data
        String callbackData = callbackQuery.getData();

        String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
        int messageId = callbackQuery.getMessage().getMessageId();

        clearPreviousMarkup(chatId, messageId);
        deletePreviousMessage(chatId, messageId);

        // Create a message object
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        String date = callbackData.replaceAll("day_", "").replaceAll(InlineCalendar.DATE_ALIAS, "");
        if (locale.equals(Locale.RU)) {
            message.setText(DateChoiceText.RU.getText() + date);
        } else {
            message.setText(DateChoiceText.EN.getText() + date);
        }
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void control(CallbackQuery callbackQuery) {
        // Handle callback data
        String callbackData = callbackQuery.getData();

        String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
        int messageId = callbackQuery.getMessage().getMessageId();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        clearPreviousMarkup(chatId, messageId);

        if (callbackData.contains("year_")) {
            LocalDate date = LocalDate.parse(callbackData.replaceAll("year_", "").replaceAll(InlineCalendar.CONTROL_ALIAS, ""));
            keyboardRowList.addAll(generateYearCalendar(date));
        } else if (callbackData.contains("month_")) {
            LocalDate date = LocalDate.parse(callbackData.replaceAll("month_", "").replaceAll(InlineCalendar.CONTROL_ALIAS, ""));
            keyboardRowList.addAll(generateMonthCalendar(date));
        } else if (callbackData.contains("decade_")) {
            LocalDate date = LocalDate.parse(callbackData.replaceAll("decade_", "").replaceAll(InlineCalendar.CONTROL_ALIAS, ""));
            keyboardRowList.addAll(generateDecadeCalendar(date));
        }
        // Create a message object
        keyboardMarkup.setKeyboard(keyboardRowList);
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(editMessageReplyMarkup);
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

    private List<List<InlineKeyboardButton>> generateDecadeCalendar(LocalDate date) {
        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();
        calendar.add(InlineCalendar.createDecadeRow(date));
        calendar.addAll(InlineCalendar.createYearsKeyboard(date));
        return calendar;
    }

    private List<List<InlineKeyboardButton>> generateYearCalendar(LocalDate date) {
        return InlineCalendar.createMonthKeyboard(date, locale);
    }

    private void clearPreviousMarkup(String chatId, int messageId) {
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

    private void deletePreviousMessage(String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
