package com.getcinderella.app.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getcinderella.app.R;

public class GridImageAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private Integer[] imgURLs;

    public GridImageAdapter(Context context, int layoutResource, Integer[] imgURLs) {
        super(context, layoutResource, imgURLs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        this.imgURLs = imgURLs;
    }

    private static class ViewHolder{
        SquareImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
        Viewholder build pattern (Similar to recyclerview)
         */
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image = (SquareImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageResource(imgURLs[position]);

        return convertView;
    }
}


