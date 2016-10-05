package self.harmony.bashrandomh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuoteAdapter extends ArrayAdapter {
    Context context;
    Typeface ubuntu;
    Typeface helvetica;

    public QuoteAdapter(Activity context, ArrayList<Quote> quotes){
        super(context, 0, quotes);
        this.context = context;
        ubuntu = Typeface.createFromAsset(context.getAssets(), "fonts/ubuntu.ttf");
        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Quote singleQuote = (Quote) getItem(position);
        TextView quoteTextView = (TextView) listItemView.findViewById(R.id.quote);
        TextView ratingTextView = (TextView) listItemView.findViewById(R.id.rating);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        TextView idTextView = (TextView) listItemView.findViewById(R.id.id);
        // Задаем шрифт courier new
        quoteTextView.setTypeface(ubuntu);
        ratingTextView.setTypeface(helvetica);
        dateTextView.setTypeface(helvetica);
        idTextView.setTypeface(helvetica);

        quoteTextView.setText(singleQuote.getText());
        ratingTextView.setText(String.valueOf(singleQuote.getRating()));
        dateTextView.setText(singleQuote.getDate());
        idTextView.setText(singleQuote.getId());


        return listItemView;
    }

    public void addNewDataToAdapter(ArrayList<Quote> list) {

    }
}
