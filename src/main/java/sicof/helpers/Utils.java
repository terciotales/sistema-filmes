package sicof.helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    public static LocalDate dateFormatter(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(s, formatter);
    }

    public static String removeSpecialCharacters(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }
}
