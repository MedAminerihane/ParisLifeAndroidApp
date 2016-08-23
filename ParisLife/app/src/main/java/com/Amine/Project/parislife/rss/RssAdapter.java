package com.Amine.Project.parislife.rss;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Amine.Project.parislife.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RssAdapter extends BaseAdapter {

	FBookTaskListener taskListener;
	private final List<RssItem> items;
	private final Context context;
	public void setTaskListener(FBookTaskListener listener)
	{
		this.taskListener = listener;
	}

	public RssAdapter(Context context, List<RssItem> items) {

		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.rss_item, null);
			holder = new ViewHolder();
			holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.itemUrl = (ImageView) convertView.findViewById(R.id.url);
			holder.itemDesc = (TextView) convertView.findViewById(R.id.itemDescription);
			holder.itemDate = (TextView) convertView.findViewById(R.id.itemDate);
			holder.share = (ImageView) convertView.findViewById(R.id.share);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemTitle.setText(items.get(position).getTitle());
		holder.itemDesc.setText(items.get(position).getDescription());
		holder.itemDate.setText(items.get(position).getDate());
		Picasso.with(context).load(items.get(position).getUrl()).error(R.drawable.photoplace)
				.into(holder.itemUrl);

		holder.share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				taskListener.postToWall(items.get(position).getLink());

			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView itemTitle,itemDesc,itemDate;
		ImageView itemUrl,share;

	}

	public static interface FBookTaskListener{

		public void postToWall(String lien);  //paramas may be added if needed
	}
}
