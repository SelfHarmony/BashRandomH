package self.harmony.bashrandomh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String HTTP_BASH_IM = "http://bash.im/random/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new BackgroundJsoup().execute(HTTP_BASH_IM);




    }

    private void updateUI(Document document) {

        TextView parsedHTML = (TextView) findViewById(R.id.sampleText);
        parsedHTML.setText(document.text());
    }

    private class BackgroundJsoup extends AsyncTask <String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {

            if (params.length < 1 || params[0] == null) {
                return null;
            }
            Document document = null;
            try {
                document = Jsoup.connect(params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            updateUI(document);
        }
    }
}
