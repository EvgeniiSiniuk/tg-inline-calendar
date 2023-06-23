package org.siniuk.calendar.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MonthAbbreviations {

    JANUARY("JAN", "ЯНВ"),
    FEBRUARY("FEB", "ФЕВ"),
    MARCH("MAR", "МАРТ"),
    APRIL("APR", "АПР"),
    MAY("MAY", "МАЙ"),
    JUNE("JUN", "ИЮНЬ"),
    JULY("JUL", "ИЮЛЬ"),
    AUGUST("AUG", "АВГ"),
    SEPTEMBER("SEP", "СЕНТ"),
    OCTOBER("OCT", "ОКТ"),
    NOVEMBER("NOV", "НОЯБ"),
    DECEMBER("DEV", "ДЕК");

    private final String englishValue;
    private final String russianValue;
}
