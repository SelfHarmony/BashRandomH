package self.harmony.bashrandomh.service.ParsingService;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Subscription;
import self.harmony.bashrandomh.R;
import self.harmony.bashrandomh.pojo.Quote;

//Created by selfharmony
public class ParsingService {

    private static ParsingService sParsingService;
    private static final int MAX_QUOTES = 10;
    public static final int min_rating = 600;
    private Subscription subscription;

    private Elements quoteTexts;
    private Elements ratings;
    private Elements date;
    private Elements id;

    private static HashMap<String, Quote> quotesFilterMap;
    private ArrayList<Quote> quotesOutputArrayList;
    private int progress;

    public ParsingService() {
        quotesOutputArrayList = new ArrayList<>(); // массив для наших объектов Quote
    }


    public ArrayList<Quote> startParsing(ResponseBody responseBody) throws IOException {

        Document doc = Jsoup.parse(responseBody.string());
        quotesOutputArrayList = parseOnePageAndFilterBest(doc);
//        System.out.println(quotesOutputArrayList);

        return quotesOutputArrayList;
//        System.out.println("time elapsed --------------  ");

//        if (subscription == null || subscription.isUnsubscribed()) {
//            subscription = HttpService.getDocumentObservable()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(document -> {
//                        parseOnePageAndFilterBest(document);
//                        System.out.println("inside parsing observable");
//                    });
//        }
    }

    private ArrayList<Quote> parseOnePageAndFilterBest(Document doc) {
        ArrayList<Quote> outputArray = new ArrayList<>();
            if (doc != null) {
                quoteTexts = doc.select("div[class=\"text\"]"); //текст
                ratings = doc.select("span[class=\"rating\"]"); //рейтинг
                date = doc.select("span[class=\"date\"]"); //дата
                id = doc.select("[class=\"id\"]"); //id <span id="v410856" class="rating">13426</span>

                int rating; //инициализируем переменную для сравнения рейтинга
                for (int i = 0; i < quoteTexts.size(); i++) { //создаем наши Quote объекты
                    rating = getValidRating(ratings, i); //получаем рейтинг, преобразовываем в integer и ловим
                    // NumberFormatException

                    if (rating >= min_rating) {
                        String quoteText = cleanPreserveLineBreaks(quoteTexts.get(i).html());//хитрый способ сохранить переводы строк
                        quoteText = removeNonParseable(quoteText);//избавляемся от недопарсенных символов
                        String quoteID = id.get(i).text();
                        String quoteDate = date.get(i).text();

                           Quote quote = new Quote(rating, quoteText, quoteID, quoteDate); //создаем объект
                        outputArray.add(quote);

                    }
                }
            }
//        ProgressBarSubscription.getProgressBehaviourSubject().onNext(outputArray.size());
        return outputArray;
    }

    public int getProgress() {
        return progress;
    }


    private int getValidRating(String ratingS) {
        int rating;
        try {
            rating = Integer.parseInt(ratingS);
        } catch (Exception e) {
            rating = Integer.parseInt(String.valueOf(R.string.settings_minRating_default));
        }
        return rating;
    }


    private int getValidRating(Elements ratings, int i) {
        int rating;
        try {
            rating = Integer.parseInt(ratings.get(i).text());
        } catch (NumberFormatException e) {
            rating = 1;
        }
        return rating;
    }

    private static String cleanPreserveLineBreaks(String bodyHtml) {

        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.parse(bodyHtml).html();
        prettyPrintedBodyFragment = Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));

        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    private String removeNonParseable(String quoteText) {
        quoteText = quoteText.replaceAll("&lt;", "<");
        quoteText = quoteText.replaceAll("&gt;", ">");
        quoteText = quoteText.replaceAll("&amp;", "&");
        //quoteText = quoteText.replaceAll("—", "-");
        return quoteText;
    }



}


