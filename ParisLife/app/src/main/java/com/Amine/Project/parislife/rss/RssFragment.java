package com.Amine.Project.parislife.rss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Amine.Project.parislife.Actu_Detail;
import com.Amine.Project.parislife.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssFragment extends Fragment implements OnItemClickListener {

	private ProgressBar progressBar;
	private ListView listView;
	private View view;
	private static final String PREF_NAME = "RegPref";
	SharedPreferences pref;

	// Editor for Shared preferences
	SharedPreferences.Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		if (view == null) {
			pref = getActivity().getApplication().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = pref.edit();
			view = inflater.inflate(R.layout.fragment_layout, container, false);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			listView = (ListView) view.findViewById(R.id.listView);

			listView.setOnItemClickListener(this);
		RssFeed rr = new RssFeed();
			rr.execute();
			//startService();
		} else {
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		return view;
	}




	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RssAdapter adapter = (RssAdapter) parent.getAdapter();
		RssItem item = (RssItem) adapter.getItem(position);
		//Uri uri = Uri.parse(item.getLink());
		Intent i =new Intent(getContext(), Actu_Detail.class);
		i.putExtra("url",item.getLink());
		//Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(i);
	}



	private class RssFeed extends AsyncTask<String, Void,List<RssItem>> {
		//RssAdapter adapter;
		@Override
		protected List<RssItem> doInBackground(String... params) {


			List<RssItem> rssItems = new ArrayList<RssItem>();
			final String RSS_LINK = "http://www.francetvinfo.fr/france.rss";
			if (isOnline()) {
				try {

					ActualiteRssParser parser = new ActualiteRssParser();
					rssItems = parser.parse(getInputStream(RSS_LINK));

					for(int i=0 ; i<10; i++) {
						editor.putString("rsstitle"+i, rssItems.get(i).getTitle());
						editor.putString("rssdesc"+i, rssItems.get(i).getDescription());
						editor.putString("rssdate"+i, rssItems.get(i).getDate());
						editor.putString("rsslink"+i, rssItems.get(i).getLink());
						editor.putString("rssurl"+i, rssItems.get(i).getUrl());
					}
					editor.commit();

				} catch (XmlPullParserException e) {
					Log.w(e.getMessage(), e);
				} catch (IOException e) {
					Log.w(e.getMessage(), e);
				}


			}
			else{

				for (int j=0;j<10;j++){

					String title =pref.getString("rsstitle"+j, "");
					String desc =pref.getString("rssdesc" + j, "");
					String date =pref.getString("rssdate" + j, "");
					String link=pref.getString("rsslink"+j, "");
					String url=pref.getString("rssurl" + j, "nophoto");
					RssItem rssItem = new RssItem(title,link,url,desc,date);
					rssItems.add(rssItem);
				}
			}
			return rssItems;

		}

		@Override
		protected void onPostExecute(List<RssItem> rssItems) {
			super.onPostExecute(rssItems);

			if (rssItems != null) {
				final RssAdapter adapter = new RssAdapter(getActivity(), rssItems);
				listView.setAdapter(adapter);
				adapter.setTaskListener(new RssAdapter.FBookTaskListener() {

					@Override
					public void postToWall(String lien) {
						ShareDialog shareDialog;
						shareDialog = new ShareDialog(getActivity());
						ShareLinkContent content = new ShareLinkContent.Builder()
								.setContentUrl(Uri.parse(lien))
								.build();

						shareDialog.show(content);
					}


				});

			} else {

				Toast.makeText(getContext(), "Error downloading rss feed", Toast.LENGTH_LONG).show();
			}
			progressBar.setVisibility(View.GONE);
		}}



		public InputStream getInputStream(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			return urlConnection.getInputStream();



		} catch (IOException e) {
			Log.w("RssApp", "Exception while retrieving the input stream", e);
			return null;
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
