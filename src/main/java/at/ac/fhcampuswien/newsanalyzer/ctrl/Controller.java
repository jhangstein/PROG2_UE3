package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsanalyzer.ui.UserInterface;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;


import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

	public static final String APIKEY = "1706c99c124a45ca89bfcbd91144b998";  //TODO add your api key -> done

	public void process(NewsApi newsApi) {
		System.out.println("Start process");

		//TODO implement Error handling -> done. throws custom Exception when getNews returns no articles
		NewsResponse newsResponse = new NewsResponse();
		try{
			newsResponse = newsApi.getNews();
			List<Article> articles = newsResponse.getArticles();
			if(articles.isEmpty()){
				throw new GetNewsException("Empty news response. Please check parameters.");
				}
		}
		catch (GetNewsException e){
			System.out.println("\n\n");
			System.out.println("No valid search results. " + e.getMessage());
			return;
		}
		//TODO load the news based on the parameters -> done
		List<Article> articles = newsResponse.getArticles();
		articles.stream().forEach(article -> System.out.println(article.toString()));


		//TODO implement methods for analysis -> done
		System.out.println();
		//Total number of articles
		System.out.println("Number of articles: " + articles.stream().count());

		// Provider with most articles
		String provider = articles.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet()
				.stream()
				.max(Comparator.comparingInt(t->t.getValue().intValue()))
				.get()
				.getKey();
		System.out.println("Provider with most articles: " + provider);

		// Author with shortest name
		Optional<String> shortest = articles.stream()
				.map(Article::getAuthor)
				.filter(Objects::nonNull)
				.min(Comparator.comparing(String::length));
		System.out.println("Author with shortest name: " + shortest);
		System.out.println();

		// Alphabetically longest title
		List<String> sorted = articles.stream()
				.map(Article::getTitle)
				.sorted(Comparator.comparing(String::length)
						.reversed()
						.thenComparing(Comparator.naturalOrder()))
				.collect(Collectors.toList());
		System.out.println("Articles sorted by length, then alphabetically: ");
		for (String s: sorted)
			System.out.println(s);

		System.out.println();
		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
