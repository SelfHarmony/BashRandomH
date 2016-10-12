package self.harmony.bashrandomh;

import android.content.*;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private static final int MAX_QUOTES = 30;
    //private static final int MIN_RATING = 8000;

    private static final String HTTP_BASH_IM = "http://bash.im/random/";
    private BashListView listView;
    private QuoteAdapter quoteAdapter;
    private TextView progressBarTextView;
    private ProgressBar progressBar;
    private ProgressBar footerProgressBar;
    private ImageView bashImage;
    private boolean flag_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface georgia = Typeface.createFromAsset(context.getAssets(), "fonts/georgia.ttf");
        TextView mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        progressBarTextView = (TextView) findViewById(R.id.textViewProgressBar);
        progressBarTextView.setTypeface(georgia);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(MAX_QUOTES); //задаем значение полного заполнения

        bashImage = (ImageView) findViewById(R.id.bashImageView);
        listView = (BashListView) findViewById(R.id.list);

        if (networkInfo != null && networkInfo.isConnected()) {
            mEmptyStateTextView.setVisibility(View.GONE);
            //выделяем отдельный объект, чтобы потом при обновлении просто запускать задачу снова -> parser.execute(HTTP_BASH_IM);
            BackgroundJsoup parser = new BackgroundJsoup();
            quoteAdapter = new QuoteAdapter(this, new ArrayList<Quote>());
            parser.execute(HTTP_BASH_IM);

            if (!quoteAdapter.isEmpty()) {


                View footer = getLayoutInflater().inflate(R.layout.footer, null);
                listView.setVisibility(View.GONE); //скрываем listView чтоб не показывать лишние элементы на стартово экране
                listView.addFooterView(footer); //впихиваем футер в наш listView
                footerProgressBar = (ProgressBar) findViewById(R.id.footerProgressBar);
                footerProgressBar.setMax(MAX_QUOTES); //задаем значение полного заполнения
                TextView footerTextView = (TextView) findViewById(R.id.footerTextView);
                footerTextView.setTypeface(georgia);
                listView.setAdapter(quoteAdapter); //задаем адаптер ПОСЛЕ установки футера

            } else {
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                hideQuotesView();
                mEmptyStateTextView.setText("Нет доступа к Bash");
            }




        } else {
            hideQuotesView();
            mEmptyStateTextView.setText(R.string.noInternet);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Quote quote = (Quote) quoteAdapter.getItem(position);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(String.valueOf(quote.getRating()), quote.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Скопировано в буфер обмена",    //((TextView) itemClicked).getText()
                        Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!flag_loading)
                    {
                        flag_loading = true;
                        addItems();

                    }
                }

            }
        });

    }

    private void hideQuotesView() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        progressBarTextView.setVisibility(View.GONE);
        bashImage.setVisibility(View.GONE);
    }

    private void addItems() {
        /*Toast.makeText(getApplicationContext(), "Подгружаем",    //((TextView) itemClicked).getText()
                Toast.LENGTH_SHORT).show();*/
        BackgroundJsoup parser = new BackgroundJsoup();
        parser.execute(HTTP_BASH_IM);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class BackgroundJsoup extends AsyncTask <String, Integer, ArrayList<Quote>> {

        @Override
        protected ArrayList<Quote> doInBackground(String... params) {


            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String stringRating = sharedPrefs.getString(
                    getString(R.string.settings_minRating_key),
                    getString(R.string.settings_minRating_default));
            int min_rating = getValidRating(stringRating);


            if (params.length < 1 || params[0] == null) {
                return null;
            }


            HashMap<String, Quote> quoteMap = new HashMap<>(); // для отсеивания дубликатов

            ArrayList<Quote> quotesList = new ArrayList<>(); // массив для наших объектов Quote
            Document doc;

            while (quoteMap.size() <= MAX_QUOTES) {

                int progress;

                doc = getJsoupData(HTTP_BASH_IM); // коннектимся к башу и получаем HTTP

                if (doc == null) {
                    return new ArrayList<>();
                } else {
                    //Получаем все элементы со страницы, из которых будем лепить объекты quote
                    Elements quoteTexts = doc.select("div[class=\"text\"]"); //текст
                    Elements ratings = doc.select("span[class=\"rating\"]"); //рейтинг
                    Elements date = doc.select("span[class=\"date\"]"); //дата
                    Elements id = doc.select("[class=\"id\"]"); //id   <span id="v410856" class="rating">13426</span>

                    int rating; //инициализируем переменную для сравнения рейтинга
                    for (int i = 0; i < quoteTexts.size(); i++) { //создаем наши Quote объекты
                        rating = getValidRating(ratings, i); //получаем рейтинг, преобразовываем в integer и ловим NumberFormatException

                        if (rating >= min_rating) {
                            String quoteText = cleanPreserveLineBreaks(quoteTexts.get(i).html());//хитрый способ сохранить переводы строк
                            quoteText = removeNonParseable(quoteText);//избавляемся от недопарсенных символов
                            String quoteID = id.get(i).text();
                            String quoteDate = date.get(i).text();

                            Quote quote = new Quote(rating, quoteText, quoteID, quoteDate); //создаем объект
                            quoteMap.put(quoteID, quote);
                            progress = quoteMap.size(); //*100/MAX_QUOTES; // а можно было проще ProgressBar.setMax(data);
                            publishProgress(progress);
                        }
                    }

                }
                //запихиваем все то добро в конечный массив
                for (Quote quote : quoteMap.values()) {
                    if (quotesList.size() > MAX_QUOTES) {
                        break;
                    }
                    quotesList.add(quote);
                }
            }
            return quotesList;
        }

        private String removeNonParseable(String quoteText) {
            quoteText = quoteText.replaceAll("&lt;", "<");
            quoteText = quoteText.replaceAll("&gt;", ">");
            quoteText = quoteText.replaceAll("&amp;", "&");
            //quoteText = quoteText.replaceAll("—", "-");
            return quoteText;
        }


        @Override
        protected void onProgressUpdate(Integer... progresses) {
            super.onProgressUpdate(progresses);
            progressBar.setProgress(progresses[0]);
            footerProgressBar.setProgress(progresses[0]);


        }

        @Override
        protected void onPostExecute(ArrayList<Quote> quotes) {
            progressBar.setVisibility(View.GONE);
            progressBarTextView.setVisibility(View.GONE);
            bashImage.setVisibility(View.GONE);
            progressBar.setProgress(0);
            quoteAdapter.addAll(quotes);
            if (footerProgressBar != null){
                footerProgressBar.setProgress(0);
            }
            flag_loading = false;
            listView.setVisibility(View.VISIBLE); //снова показываем наш listView
        }

        private Document getJsoupData(String http) {
            Document doc = null;
            try {
                doc = Jsoup.connect(http).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
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

        private int getValidRating(String ratingS) {
            int rating;
            try {
                rating = Integer.parseInt(ratingS);
            } catch (Exception e) {
                rating = Integer.parseInt(String.valueOf(R.string.settings_minRating_default));
            }
            return rating;
        }
    }



    private static String cleanPreserveLineBreaks(String bodyHtml) {

        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.parse(bodyHtml).html();
        prettyPrintedBodyFragment = Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));

        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
