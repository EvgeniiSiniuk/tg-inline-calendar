package org.siniuk.calendar.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DateChoiceText {

    EN("The date you have chosen is "),
    RU("Дата которую вы выбрали ");

    private final String text;
}
