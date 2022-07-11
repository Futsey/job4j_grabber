package parser;

import utils.DateTimeParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser  implements DateTimeParser {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public LocalDateTime parse(String strToParse) {
        return LocalDateTime.parse(strToParse, FORMATTER);
    }
}
