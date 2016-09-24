package self.harmony.bashrandomh;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TextParser {
    static int progress;
    public static final int MAX_QUOTES = 50;
    public static final int MIN_RATING = 12000;

    private TextParser() {}

    public static ArrayList<Quote> fetchQuotesList(String url) {
        ArrayList<Quote> quotesList = new ArrayList<>();
        Document doc = null;

        while (quotesList.size() <= MAX_QUOTES) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            doc.outputSettings().prettyPrint(false);

            Elements quoteTexts = doc.select("div[class=\"text\"]"); //текст
            Elements ratings = doc.select("span[class=\"rating\"]"); //рейтинг

            for (int i = 0; i < quoteTexts.size(); i++) {
                int rating = 1;
                String text = cleanPreserveLineBreaks(quoteTexts.get(i).html());
                try {
                    rating = Integer.parseInt(ratings.get(i).text());
                } catch (NumberFormatException e) {
                    rating = 1;
                } finally {
                    if (rating >= MIN_RATING) {
                        Quote quote = new Quote(rating, text);
                        if (!isDuplicate(quote, quotesList)) {
                            quotesList.add(quote);
                            progress = quotesList.size();

                        }
                    }
                    if (quotesList.size() >= MAX_QUOTES) {
                        break;
                    }
                }
            }
        }

        return quotesList;
    }

    private static boolean isDuplicate(Quote quote, ArrayList<Quote> quotesList) {
        boolean toReturn = false;

        for (Quote compar : quotesList) {
            if (compar.theSameAs(quote)) {
                toReturn = true;
            }
        }
        
        return toReturn;
    }

    public static String cleanPreserveLineBreaks(String bodyHtml) {

        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.clean(bodyHtml, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
