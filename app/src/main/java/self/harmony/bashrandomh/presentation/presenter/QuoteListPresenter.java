package self.harmony.bashrandomh.presentation.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import self.harmony.bashrandomh.pojo.Quote;
import self.harmony.bashrandomh.presentation.view.QuoteListView;

@InjectViewState
public class QuoteListPresenter extends MvpPresenter<QuoteListView> {

    private static final String QUOTE_TEXT = "div[class=\"text\"]";
    private static final String QUOTE_RATING = "span[class=\"rating\"]";
    private static final String QUOTE_DATE = "span[class=\"date\"]";
    private static final String QUITE_ID = "[class=\"id\"]";
    private static final String HTTP_BASH_IM = "http://bash.im/random/";
    private static final int MAX_QUOTES = 30;

    private String stringRating;

    private int minRating;
    private HashMap<String, Quote> quoteMap = new HashMap<>(); // для отсеивания дубликатов
    private ArrayList<Quote> quotesList = new ArrayList<>(); // массив для наших объектов Quote
    private Document doc;


    private Subscription subscription;
    private int mProgress;
    private static final BehaviorSubject<Integer> ProgressSubject = BehaviorSubject.create(0);
    private boolean bashIsAvailable;
    private Context activityContext;


    public QuoteListPresenter() {
        initFields();

    }

    private void initFields() {
        quoteMap = new HashMap<>();
        quotesList = new ArrayList<>();


    }


    private ArrayList<Quote> parseBashIntoQuoteList() {
        getViewState().setFieldsForPresenter();
        while (quoteMap.size() <= MAX_QUOTES && bashIsAvailable) {

            doc = getJsoupData(HTTP_BASH_IM); // коннектимся к башу и получаем HTTP

            if (doc != null) {
                getViewState().setBashAvailability(true);
                //Получаем все элементы со страницы, из которых будем лепить объекты quote
                Elements quoteTexts = doc.select(QUOTE_TEXT); //текст
                Elements ratings = doc.select(QUOTE_RATING); //рейтинг
                Elements date = doc.select(QUOTE_DATE); //дата
                Elements id = doc.select(
                        QUITE_ID); //id   <span id="v410856" class="rating">13426</span>

                int rating; //инициализируем переменную для сравнения рейтинга
                for (int i = 0; i < quoteTexts.size(); i++) { //создаем наши Quote объекты
                    rating = getValidRating(ratings,
                            i); //получаем рейтинг, преобразовываем в integer и ловим
                    // NumberFormatException

                    if (rating >= minRating) {
                        String quoteText = cleanPreserveLineBreaks(
                                quoteTexts.get(i).html());//хитрый способ сохранить переводы строк
                        quoteText = removeNonParseable(
                                quoteText);//избавляемся от недопарсенных символов
                        String quoteID = id.get(i).text();
                        String quoteDate = date.get(i).text();

                        Quote quote = new Quote(rating, quoteText, quoteID,
                                quoteDate); //создаем объект
                        quoteMap.put(quoteID, quote);
                        mProgress = quoteMap.size(); //*100/MAX_QUOTES; // а можно было проще

                        ProgressSubject.onNext(mProgress);

                        //ProgressSubjectSingleton.setValue(mProgress);
                    }
                }

            } else { //если баш недоступен, кладем в список пустую цитату
                getViewState().setBashAvailability(false);
                quoteMap.values().add(new Quote(0, "", "", ""));
            }
        }
        //запихиваем все то добро в конечный массив
        for (Quote quote : quoteMap.values()) {
            if (quotesList.size() > MAX_QUOTES) {
                break;
            }
            quotesList.add(quote);
        }

        return quotesList;
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



    private String removeNonParseable(String quoteText) {
        quoteText = quoteText.replaceAll("&lt;", "<");
        quoteText = quoteText.replaceAll("&gt;", ">");
        quoteText = quoteText.replaceAll("&amp;", "&");
        //quoteText = quoteText.replaceAll("—", "-");
        return quoteText;
    }

    private static String cleanPreserveLineBreaks(String bodyHtml) {

        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.parse(bodyHtml).html();
        prettyPrintedBodyFragment = Jsoup.clean(prettyPrintedBodyFragment, "",
                Whitelist.none().addTags("br", "p"),
                new Document.OutputSettings().prettyPrint(true));

        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(),
                new Document.OutputSettings().prettyPrint(false));
    }

    /////////////////// CONNECTION ////////////////////////
    private Document getJsoupData(String http) {
        Document doc = null;
        try {
            doc = Jsoup.connect(http).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /////////////////// RX SUBSCRIPTIONS ////////////////////////
    public void subscribeToQuotesArray() {
        if (subscription == null || subscription.isUnsubscribed()) {
            subscription = ProgressSubject.asObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(progress -> {

                    }, Throwable::printStackTrace);
        }
    }

    public void setBashIsAvailable(boolean bashIsAvailable) {
        this.bashIsAvailable = bashIsAvailable;
    }

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }
}
