package com.chen.develop.zhihudaily.Adapter;

import android.support.v7.widget.RecyclerView;

import com.chen.develop.zhihudaily.Bean.CityListBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class CityListAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<CityListBean> items = new ArrayList<CityListBean>();

  public CityListAdapter() {
    setHasStableIds(true);
  }

  public void add(CityListBean object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, CityListBean object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends CityListBean> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(CityListBean... items) {
    addAll(Arrays.asList(items));
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public void remove(String object) {
    items.remove(object);
    notifyDataSetChanged();
  }

  public CityListBean getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
