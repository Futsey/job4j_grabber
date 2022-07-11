package parser;

import org.jsoup.Jsoup;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.DateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    private static final String PAGE_NUMBER = "?page=";

    public static void main(String[] args) throws IOException {
        HabrCareerParse myCreer = new HabrCareerParse();
        int page = 1;
        do {
            Connection connection = Jsoup.connect(PAGE_LINK + PAGE_NUMBER + page);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element titleDate = row.select(".vacancy-card__date").first();
                Element linkDate = titleDate.child(0);
                String vacancyName = linkElement.text();
                String date = linkDate.attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK,
                        linkElement.attr("href"),
                        linkDate.attr("time"));
                System.out.printf("%s" + " | " +  "%s" + " | " +  "%s" + System.lineSeparator() +  "%s%n%n",
                        vacancyName,
                        link,
                        date,
                        myCreer.retrieveDescription(link));
            });
            page++;
        } while (page != 2);

    }

    private String retrieveDescription(String link) {
        try {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Element elem = document.select(".collapsible-description__content").first();
            link = elem.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return link;
    }
}
