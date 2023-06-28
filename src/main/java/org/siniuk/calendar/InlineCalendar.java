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

    public static final String CONTROL_ALIAS = "_control_al";
    public static final String DATE_ALIAS = "_date_al";

    protected static List<InlineKeyboardButton> createDecadeRow(LocalDate date) {
        List<InlineKeyboardButton> yearAndMonthRow = new ArrayList<>();
        String buttonName;
        int nextDecade = date.getYear() + 8;
        int prevDecade = date.getYear() - 8;
        buttonName = prevDecade + "-" + date.getYear();

        String prevDecadeCallbackData = prevDecade + "-01-01";
        String nextDecadeCallbackData = nextDecade + "-01-01";

        yearAndMonthRow.add(ButtonFactory.createButton("<<<", "decade_" + prevDecadeCallbackData + CONTROL_ALIAS));
        yearAndMonthRow.add(ButtonFactory.createButton(buttonName, "_"));
        yearAndMonthRow.add(ButtonFactory.createButton(">>>", "decade_" + nextDecadeCallbackData + CONTROL_ALIAS));
        return yearAndMonthRow;
    }

    protected static List<List<InlineKeyboardButton>> createYearsKeyboard(LocalDate date) {
        List<List<InlineKeyboardButton>> yearsKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        String callbackDataStartOfYear = "-01-01";

        for (int i = 8; i >= 6; i--) {
            int year = date.getYear() - i;
            firstRow.add(ButtonFactory.createButton(String.valueOf(year), "year_" + year + callbackDataStartOfYear + CONTROL_ALIAS));
        }
        for (int i = 5; i >= 3; i--) {
            int year = date.getYear() - i;
            secondRow.add(ButtonFactory.createButton(String.valueOf(year), "year_" + year + callbackDataStartOfYear + CONTROL_ALIAS));
        }
        for (int i = 2; i >= 0; i--) {
            int year = date.getYear() - i;
            thirdRow.add(ButtonFactory.createButton(String.valueOf(year), "year_" + year + callbackDataStartOfYear + CONTROL_ALIAS));
        }
        yearsKeyboard.add(firstRow);
        yearsKeyboard.add(secondRow);
        yearsKeyboard.add(thirdRow);
        return yearsKeyboard;
    }

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
            int prevYear = date.getYear() - 1;
            prevMonthCallbackData = prevYear + "-12-01";
            nextMonthCallbackData = date.getYear() + "-02-01";
        } else if (date.getMonthValue() == 12) {
            int nextYear = date.getYear() + 1;
            prevMonthCallbackData = date.getYear() + "-11-01";
            nextMonthCallbackData = nextYear + "01-01";
        } else {
            int nextMonth = date.getMonthValue() + 1;
            int prevMonth = date.getMonthValue() - 1;
            prevMonthCallbackData = date.getYear() + "-" + toTwoDigitNumber(prevMonth) + "-01";
            nextMonthCallbackData = date.getYear() + "-" + toTwoDigitNumber(nextMonth) + "-01";
        }
        yearAndMonthRow.add(ButtonFactory.createButton("<<<", "month_" + prevMonthCallbackData + CONTROL_ALIAS));
        yearAndMonthRow.add(ButtonFactory.createButton(buttonName, "year_" + date.getYear() + "-01-01" + CONTROL_ALIAS));
        yearAndMonthRow.add(ButtonFactory.createButton(">>>", "month_" + nextMonthCallbackData + CONTROL_ALIAS));
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

        yearRow.add(ButtonFactory.createButton("<<<", "year_" + prevYear + callbackDataStartOfYear + CONTROL_ALIAS));
        yearRow.add(ButtonFactory.createButton(String.valueOf(date.getYear()), "decade_" + date.getYear() + callbackDataStartOfYear + CONTROL_ALIAS));
        yearRow.add(ButtonFactory.createButton(">>>", "year_" + nextYear + callbackDataStartOfYear + CONTROL_ALIAS));

        for (int i = 1; i < 5; i++) {
            String callbackDataStartOfMonth = "-0" + i + "-01";
            if (locale.equals(Locale.RU)) {
                firstRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
            } else {
                firstRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
            }
        }
        for (int i = 5; i < 9; i++) {
            String callbackDataStartOfMonth = "-0" + i + "-01";
            if (locale.equals(Locale.RU)) {
                secondRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
            } else {
                secondRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
            }
        }
        for (int i = 9; i < 13; i++) {
            String callbackDataStartOfMonth;
            callbackDataStartOfMonth = "-" + toTwoDigitNumber(i) + "-01";
            if (locale.equals(Locale.RU)) {
                thirdRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getRussianValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
            } else {
                thirdRow.add(ButtonFactory.createButton(monthAbbreviations[i - 1].getEnglishValue(), "month_" + date.getYear() + callbackDataStartOfMonth + CONTROL_ALIAS));
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

        int firsDayOfMonth = monthDate.getDayOfWeek().getValue() - 1;
        int lastDayOfMonth = lastDayMonth.getDayOfWeek().getValue() - 1;
        List<List<InlineKeyboardButton>> monthKeyboard = new ArrayList<>();
        int dayOfMonth = 1;
        for (int weekNumber = 0; weekNumber < howMuchWeeks; weekNumber++) {
            List<InlineKeyboardButton> weekRow = new ArrayList<>();
            for (int dayNumber = 0; dayNumber < 7; dayNumber++) {
                String callbackData = date.getYear() + "-" + toTwoDigitNumber(date.getMonthValue()) + "-";
                if (weekNumber == 0) {
                    if (dayNumber < firsDayOfMonth) {
                        weekRow.add(ButtonFactory.createButton(" ", "_"));
                    } else {
                        weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + toTwoDigitNumber(dayOfMonth) + DATE_ALIAS));
                        dayOfMonth++;
                    }
                } else if (weekNumber == howMuchWeeks - 1) {
                    if (dayNumber > lastDayOfMonth) {
                        weekRow.add(ButtonFactory.createButton(" ", "_"));
                    } else {
                        weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + toTwoDigitNumber(dayOfMonth) + DATE_ALIAS));
                        dayOfMonth++;
                    }
                } else {
                    weekRow.add(ButtonFactory.createButton(String.valueOf(dayOfMonth), "day_" + callbackData + toTwoDigitNumber(dayOfMonth) + DATE_ALIAS));
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
        return (int) (Math.ceil((double) tmp / 7) + 1);
    }

    private static String toTwoDigitNumber(int number) {
        return String.format("%02d", number);
    }
}
