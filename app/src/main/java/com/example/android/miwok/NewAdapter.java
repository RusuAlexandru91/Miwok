package com.example.android.miwok;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewAdapter extends ArrayAdapter<New> {

    // Define variable for color so we can change the bk color dinamicaly
    private int mColorResurceId;

    public NewAdapter(Activity context, ArrayList<New> number, int colorResurceId) {
        super(context, 0, number);
        mColorResurceId = colorResurceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        New currentNew = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentNew.getMiwokTranslationl());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.english_text_view);
        defaultTextView.setText(currentNew.getDefaultTranslation());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        if (currentNew.hasImage()) {
            imageView.setImageResource(currentNew.getmImageResourceId());
        } else {
            imageView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResurceId);
        textContainer.setBackgroundColor(color);

        return listItemView;
    }
}
