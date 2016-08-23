package com.Amine.Project.parislife.rss;


public class RssItem {

	private final String title;
	private final String link;
	private final String url;
	private final String description;
	private final String date;

	public RssItem(String title, String link, String url,String description,String date) {
		this.title = title;
		this.link = link;
		this.url=url;
		this.description=description;
		this.date=date;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}
	public String getUrl(){
		return url;
	}
	public String getDescription(){
		return description;
	}
	public String getDate(){
		return date;
	}

}
