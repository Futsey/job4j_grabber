package parser;

import org.jsoup.Jsoup;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int PAGES_TO_PARSE = 5;
    private static final Logger LOG = LoggerFactory.getLogger(HabrCareerParse.class.getName());

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrCareerParse = new HabrCareerParse();
        for (Post vacancy : habrCareerParse.list(PAGE_LINK)) {
            System.out.println(vacancy);
        }
    }

    private String retrieveDescription(String link) {
        try {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Element elem = document.select(".collapsible-description__content").first();
            link = elem.text();
        } catch (Exception e) {
            LOG.error("Exception. See the logfile", e);
            e.printStackTrace();
        }
        return link;
    }

    private Post postParser(Element el) {
        HabrCareerParse myCareer = new HabrCareerParse();
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        Element titleElement = el.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        Element titleDate = el.select(".vacancy-card__date").first();
        Element linkDate = titleDate.child(0);
        String vacancyName = linkElement.text();
        String date = linkDate.attr("datetime");
        String link = String.format("%s%s", SOURCE_LINK,
                linkElement.attr("href"),
                linkDate.attr("time"));
        Post post = new Post(
                vacancyName,
                link,
                myCareer.retrieveDescription(link),
                timeParser.parse(date));
        return post;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> jobList = new ArrayList<>();
        try {
            for (int i = 1; i <= PAGES_TO_PARSE; i++) {
                Connection connection = Jsoup.connect(link + i);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> jobList.add(postParser(row)));
            }
        } catch (IllegalArgumentException iae) {
            LOG.error("IllegalArgumentException. See the logfile", iae);
            throw new IllegalArgumentException();
        }
        return jobList;
    }
}
