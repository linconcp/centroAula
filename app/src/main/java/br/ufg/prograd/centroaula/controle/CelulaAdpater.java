package br.ufg.prograd.centroaula.controle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by linconcp on 05/05/2016.
 */
public class CelulaAdpater extends BaseAdapter {

  private Context mContext;

  public CelulaAdpater(Context c) {
    mContext = c;
  }

  @Override
  public int getCount() {
    return 0;
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
    TextView imageView;
    if (convertView == null) {
      // if it's not recycled, initialize some attributes
      imageView = new TextView(mContext);
      imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
      imageView.setPadding(6, 6, 6, 6);
    } else {
      imageView = (TextView) convertView;
    }

    imageView.setText(mThumbIds[position]);
    return imageView;  }
}
