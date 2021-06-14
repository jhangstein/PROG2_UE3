package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class UserInterface 
{
	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("crypto")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.createNewsApi();
		ctrl.process(newsApi);
	}

	public void getDataFromCtrl2(){
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("corona")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setSourceCategory(Category.health)
				.createNewsApi();
		ctrl.process(newsApi);
	}

	public void getDataFromCtrl3(){
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("football")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCategory(Category.sports)
				.setLanguage(Language.en)
				.createNewsApi();
		ctrl.process(newsApi);
	}
	
	public void getDataForCustomInput1() {
		Scanner scan = new Scanner(System.in);
		System.out.print("What news would you like to view? Please enter a query (in english): ");
		String query = scan.nextLine();

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ(query)
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setLanguage(Language.en)
				.createNewsApi();
		ctrl.process(newsApi);
	}

	public void getDataForCustomInput2()  {
		Scanner scan = new Scanner(System.in);

		System.out.print("What news would you like to view? Please enter a query: ");
		String query = scan.nextLine();

		System.out.print("Please specify a source country for your news (two letter country code): ");
		String country = scan.nextLine();
		Country inputCountry = checkInputCountry(country);
		if(inputCountry == null){
			System.out.println("Invalid Country code. Please try again.");
			System.out.println("Valid country codes: " + Arrays.toString(Country.values()));
			getDataForCustomInput2();
		}

		System.out.print("Please choose a category: " + Arrays.toString(Category.values()));
		System.out.println();
		String category = scan.nextLine();
		Category inputCategory = checkInputCategory(category);
		if(inputCategory == null){
			System.out.println("Invalid Category. Please try again.");
		}

		System.out.print("Please specify a language (two letter country code): ");
		String language = scan.nextLine();
		Language inputLanguage = checkInputLanguage(language);
		if(inputLanguage == null){
			System.out.println("Invalid language. Please try again.");
			System.out.println("Valid country codes: " + Arrays.toString(Language.values()));
			getDataForCustomInput2();
		}

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ(query)
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(inputCountry)
				.setSourceCategory(inputCategory)
				.setLanguage(inputLanguage)
				.createNewsApi();
		ctrl.process(newsApi);
	}

	public static Country checkInputCountry (String test) {
		for (Country c : Country.values()) {
			if (c.name().equals(test)) {
				return c;
			}
		}
		return null;
	}
	public static Category checkInputCategory (String test) {
		for (Category c : Category.values()) {
			if (c.name().equals(test)) {
				return c;
			}
		}
		return null;
	}
	public static Language checkInputLanguage (String test) {
		for (Language c : Language.values()) {
			if (c.name().equals(test)) {
				return c;
			}
		}
		return null;
	}



	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Global Crypto news", this::getDataFromCtrl1);
		menu.insert("b", "Austria Corona News", this::getDataFromCtrl2);
		menu.insert("c", "Football News in english", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input for english global news headlines",this::getDataForCustomInput1);
		menu.insert("e", "Choice User Input (set query, country, category and language)",this::getDataForCustomInput2);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
