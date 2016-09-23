package self.harmony.bashrandomh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String HTTP_BASH_IM = "http://bash.im/random/";
    ListView listView;
    QuoteAdapter quoteAdapter;
    ArrayList<Quote> quotesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);




        new BackgroundJsoup().execute(HTTP_BASH_IM);




    }

    private void updateUI(ArrayList<Quote> quotes) {

        quoteAdapter = new QuoteAdapter(this, quotes);
        listView.setAdapter(quoteAdapter);

    }

    private class BackgroundJsoup extends AsyncTask <String, Void, ArrayList<Quote>> {

        @Override
        protected ArrayList<Quote> doInBackground(String... params) {

            if (params.length < 1 || params[0] == null) {
                return null;
            }


            ArrayList<Quote> quotes = TextParser.fetchQuotesList(params[0]);
            return quotes;
        }

        @Override
        protected void onPostExecute(ArrayList<Quote> quotes) {
            updateUI(quotes);
        }
    }
}
