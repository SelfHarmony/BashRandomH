package self.harmony.bashrandomh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import self.harmony.bashrandomh.R;
import self.harmony.bashrandomh.Views.FontedTextView;
import self.harmony.bashrandomh.pojo.Quote;

public class QuoteAdapter extends ArrayAdapter {
    Context context;


    public QuoteAdapter(Activity context, ArrayList<Quote> quotes){
        super(context, 0, quotes);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Quote singleQuote = (Quote) getItem(position);
        FontedTextView quoteTextView = (FontedTextView) listItemView.findViewById(R.id.quote);
        FontedTextView ratingTextView = (FontedTextView) listItemView.findViewById(R.id.rating);
        FontedTextView dateTextView = (FontedTextView) listItemView.findViewById(R.id.date);
        FontedTextView idTextView = (FontedTextView) listItemView.findViewById(R.id.id);

        quoteTextView.setText(singleQuote.getText());
        ratingTextView.setText(String.valueOf(singleQuote.getRating()));
        dateTextView.setText(singleQuote.getDate());
        idTextView.setText(singleQuote.getId());


        return listItemView;
    }

    public void addNewDataToAdapter(ArrayList<Quote> list) {

    }
}
