package com.madventures.sawmalie.madventures;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Image Adapter that is used to populate the GridView in the main Activity.
 */
public class ImageAdapter extends BaseAdapter {
    private Context myContext;

    // array containing images
    private Integer[] imageRefs = { R.drawable.itinerary, R.drawable.today, R.drawable.tip, R.drawable.conv,
            R.drawable.nearme, R.drawable.weather, R.drawable.currency, R.drawable.budget, R.drawable.local,
            R.drawable.poi, R.drawable.manage };

    /**
     * Constructor for Image adapter, sets the context.
     *
     * @param c
     *            Context
     */
    public ImageAdapter(Context c) {
        this.myContext = c;
    }

    @Override
    public int getCount() {
        return imageRefs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(myContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(80, 80, 80, 80);
        }

        else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageRefs[position]);

        return imageView;
    }
}
