package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "1706c99c124a45ca89bfcbd91144b998";  //TODO add your api key -> done

	public void process(NewsApi newsApi) {
		System.out.println("Start process");
		NewsResponse newsResponse = new NewsResponse();
		List<Article> articles = new ArrayList<>();
		try{
			newsResponse = newsApi.getNews();
			articles = newsResponse.getArticles();
/*			if(articles.isEmpty()){
				throw new GetNewsException("Empty news response. Please check parameters.");
				}*/
		}
		catch (GetNewsException e){
			System.out.println("\n\n");
			System.out.println("No valid search results. " + e.getMessage());
			return;
		}
		//TODO load the news based on the parameters -> done
//		List<Article> articles = newsResponse.getArticles();
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

		System.out.print("Would you like to save the articles to your device? [y|n]");
		Scanner scan = new Scanner(System.in);
		String answer = scan.nextLine();
		if (answer.equals("y")){
			for (Article article: articles) {
				try{
					URL url = new URL(article.getUrl());
					InputStream input = url.openStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(input));
					BufferedWriter bw = new BufferedWriter(new FileWriter(article.getTitle().substring(0, 5) + ".html"));
					String line;
					while ((line = br.readLine()) != null) {
						bw.write(line);
						bw.newLine();
					}
					br.close();
					bw.close();
					System.out.println("Article successfully saved!");
				} catch (IOException e) {
					System.out.println("I/O failed. Maybe title too short?");
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("RIP");
					e.printStackTrace();
				}
			}
		}
		System.out.println("End process");
	}
	

	public Object getData() {
		return null;
	}
}
