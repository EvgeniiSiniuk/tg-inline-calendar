package org.siniuk.calendar;

import org.siniuk.calendar.utils.ButtonFactory;
import org.siniuk.calendar.utils.DaysAbbreviations;
import org.siniuk.calendar.utils.Locale;
import org.siniuk.calendar.utils.MonthAbbreviations;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InlineCalendar {

    protected static List<InlineKeyboardButton> createYearAndMonthRow(LocalDate date, Locale locale) {
        List<InlineKeyboardButton> yearAndMonthRow = new ArrayList<>();
        String buttonName;
        if (locale.equals(Locale.RU)) {
            buttonName = date.getYear() + " " + MonthAbbreviations.valueOf(date.getMonth().toString()).getRussianValue();
        } else {
            buttonName = date.getYear() + " " + MonthAbbreviations.valueOf(date.getMonth().toString()).getEnglishValue();
        }
        String prevMonthCallbackData;
        String nextMonthCallbackData;

        if (date.getMonthValue() == 1) {
            prevMonthCallbackData = date.getYear() - 1 + "-12-01";
            nextMonthCallbackData = date.getYear() + "-02-01";
        } else if (date.getMonthValue() == 12) {
            prevMonthCallbackData = date.getYear() + "-11-01";
            nextMonthCallbackData = date.getYear() + 1 + "01-01";
        } else {
            prevMonthCallbackData = date.getYear() + "-" + date.getMonthValue() + 1 + "-01";
            nextMonthCallbackData = date.getYear() + "-" + date.getMonthValue() + 1 + "-01";
        }
        yearAndMonthRow.add(ButtonFactory.createButton("<<<", prevMonthCallbackData));
        yearAndMonthRow.add(ButtonFactory.createButton(buttonName, "_"));
        yearAndMonthRow.add(ButtonFactory.createButton(">>>", nextMonthCallbackData));
        return yearAndMonthRow;
    }

    protected static List<List<InlineKeyboardButton>> createMonthKeyboard(LocalDate date, Locale locale) {
        List<List<InlineKeyboardButton>> monthKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> yearRow = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        MonthAbbreviations[] monthAbbreviations = MonthAbbreviations.values();

        String callbackDataStartOfYear = "-01-01";
        int nextYear = date.getYear() + 1;
        int prevYear = date.getYear() - 1;

        yearRow.add(ButtonFactory.createButton("<<<", "year_" + prevYear + callbackDataStartOfYear));
        yearRow.add(ButtonFactory.createButton(String.valueOf(date.getYear()), "_"));
        yearRow.add(ButtonFactory.createButton(">>>", "year_" + nextYear + callbackDataStartOfYear));

        for (int i = 0; i < 4; i++) {
            String callbackDataStartOfMonth = "-0" + i + "-01";
            if (locale.equals(Locale.RU)) {
                firstRow.add(ButtonFactory.createButton(monthAbbreviations[i].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            } else {
                firstRow.add(ButtonFactory.createButton(monthAbbreviations[i].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            }
        }
        for (int i = 4; i < 8; i++) {
            String callbackDataStartOfMonth = "-0" + i + "-01";
            if (locale.equals(Locale.RU)) {
                secondRow.add(ButtonFactory.createButton(monthAbbreviations[i].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            } else {
                secondRow.add(ButtonFactory.createButton(monthAbbreviations[i].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            }
        }
        for (int i = 8; i < 12; i++) {
            String callbackDataStartOfMonth;
            if (i < 10) {
                callbackDataStartOfMonth = "-0" + i + "-01";
            } else {
                callbackDataStartOfMonth = "-" + i + "-01";
            }
            if (locale.equals(Locale.RU)) {
                thirdRow.add(ButtonFactory.createButton(monthAbbreviations[i].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            } else {
                thirdRow.add(ButtonFactory.createButton(monthAbbreviations[i].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth));
            }
        }
        monthKeyboard.add(yearRow);
        monthKeyboard.add(firstRow);
        monthKeyboard.add(secondRow);
        monthKeyboard.add(thirdRow);
        return monthKeyboard;
    }

    protected static List<InlineKeyboardButton> createDayOfWeekRow(Locale locale) {
        List<InlineKeyboardButton> daysOfWeek = new ArrayList<>();
        for (DaysAbbreviations day : DaysAbbreviations.values()) {
            if (locale.equals(Locale.RU)) {
                daysOfWeek.add(ButtonFactory.createButton(day.getRussianValue(), "_"));
            } else {
                daysOfWeek.add(ButtonFactory.createButton(day.getEnglishValue(), "_"));
            }
        }
        return daysOfWeek;
    }

    protected static List<List<InlineKeyboardButton>> createDayOfMonthKeyboard(LocalDate date) {
        LocalDate monthDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate lastDayMonth = monthDate.withDayOfMonth(monthDate.lengthOfMonth());

        int howMuchWeeks = howMuchWeeksInMonth(monthDate);

        int firsDayOfMonth = date.getDayOfWeek().getValue() - 1;
        int lastDayOfMonth = lastDayMonth.getDayOfWeek().getValue() - 1;
        List<List<InlineKeyboardButton>> monthKeyboard = new ArrayList<>();
        int dayOfMonth = 1;
        for (int weekNumber = 0; weekNumber < howMuchWeeks; weekNumber++) {
            List<InlineKeyboardButton> weekRow = new ArrayList<>();
            for(int dayNumber = 0; dayNumber < 7; dayNumber++) {
                String callbackData = date.getYear() + "-" + date.getMonthValue() + "-";
                if (weekNumber == 0) {
                    if(dayNumber < firsDayOfMonth) {
                        weekRow.add(ButtonFactory.createButton("", ""));
                    } else {
                        weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + dayOfMonth));
                        dayOfMonth++;
                    }
                } else if (weekNumber == howMuchWeeks - 1) {
                    if (dayNumber > lastDayOfMonth) {
                        weekRow.add(ButtonFactory.createButton("", ""));
                    } else {
                        weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + dayOfMonth));
                        dayOfMonth++;
                    }
                } else {
                    weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + dayOfMonth));
                    dayOfMonth++;
                }
            }
            monthKeyboard.add(weekRow);
        }
        return monthKeyboard;
    }

    private static int howMuchWeeksInMonth(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        int tmp = date.lengthOfMonth() - 8 + dayOfWeek;
        return (int) (Math.ceil((double) tmp/7) + 1);
    }
}
