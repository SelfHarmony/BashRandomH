package self.harmony.bashrandomh;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    public static final int MAX_QUOTES = 95;
    public static final int MIN_RATING = 9000;

    public static final String HTTP_BASH_IM = "http://bash.im/random/";
    ListView listView;
    QuoteAdapter quoteAdapter;
    TextView progressBarTextView;
    ProgressBar progressBar;
    ImageView bashImage;
    BackgroundJsoup parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        progressBarTextView = (TextView) findViewById(R.id.textViewProgressBar);
//        progressBarTextView.setText("Отбираем цитаты с рейтингом выше " + MIN_RATING + "...");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bashImage = (ImageView) findViewById(R.id.bashImageView);

        listView = (ListView) findViewById(R.id.list);

        //выделяем отдельный объект, чтобы потом при обновлении просто запускать задачу снова -> parser.execute(HTTP_BASH_IM);
        parser = new BackgroundJsoup();
        parser.execute(HTTP_BASH_IM);

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


    }

    private void updateUI(ArrayList<Quote> quotes) {

        quoteAdapter = new QuoteAdapter(this, quotes);
        listView.setAdapter(quoteAdapter);


    }

    private class BackgroundJsoup extends AsyncTask <String, Integer, ArrayList<Quote>> {

        @Override
        protected ArrayList<Quote> doInBackground(String... params) {

            if (params.length < 1 || params[0] == null) {
                return null;
            }

            ArrayList<Quote> quotesList = new ArrayList<>();
            Document doc = null;

            while (quotesList.size() <= MAX_QUOTES) {
                int progress;
                try {
                    doc = Jsoup.connect(HTTP_BASH_IM).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                                publishProgress(progress);
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

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            super.onProgressUpdate(progresses);
            progressBar.setProgress(progresses[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<Quote> quotes) {
            progressBar.setVisibility(View.GONE);
            progressBarTextView.setVisibility(View.GONE);
            bashImage.setVisibility(View.GONE);
            progressBar.setProgress(0);
            updateUI(quotes);
        }
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
