package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.geometry.Orientation;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.IllegalArgumentException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.TextAlignment;
import java.io.FileInputStream;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.control.ComboBox;
import java.util.concurrent.TimeUnit;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    Stage stage;
    Scene scene;
    VBox root;
    HBox hbox;
    Button convert;
    TextField amount;
    //Label title;
    TextField currency;
    //Label content;
    TextField startingCurrency;
    Label result;
    Separator separator;
    Separator separator2;
    Label warning;
    String ip;
    String city;
    String region;
    String country;
    String currencyCode;
    String testCurrencyCode;
    String stxt;
    Double checkRate;
    ComboBox<String> startCurrency;
    ComboBox<String> endCurrency;
    HBox bottom;
    Label test;
    Label explain;
    Button testButton;
    TextField testIP;
    Separator separator3;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        hbox = new HBox(8);
        separator = new Separator(Orientation.HORIZONTAL);
        separator2 = new Separator(Orientation.HORIZONTAL);
        convert = new Button("Convert");
        amount = new TextField("Amount of Starting Currency (Enter Number)");
        //title = new Label("Title of Article Goes Here");
        currency = new TextField("Currency to Convert To (Currency Code)");
        //content = new Label("Article Content Goes Here");
        stxt = "Or  Enter a Currency to Convert From (Currency Code)";
        startingCurrency = new TextField(stxt);
        result = new Label("Result Goes Here");
        warning = new Label("Please limit converts to one hit per second");
        startCurrency = new ComboBox<>();
        endCurrency = new ComboBox<>();
        //link = new Label("Article Link Goes Here");
        separator3 = new Separator(Orientation.HORIZONTAL);
        bottom = new HBox(8);
        test = new Label("Test Case. Use this to test other IP Addresses as starting currencies.");
        String ts = "Type custom IP Adress here. Use above controls for the rest of the parameters";
//        explain = new Label("Use above controls to set the rest of the parameters");
        testIP = new TextField(ts);
        testButton = new Button("Test Convert");
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {

        startCurrency.getItems().addAll("USD", "EUR", "JPY", "INR", "CNH", "MXN", "CAD", "GBP",
            "SAR", "AUD", "CHF", "NZD", "HKD", "PKR", "BRL", "Current Currency (IP Location)");
        startCurrency.setValue("Starting Currency");
        //startCurrency.setValue("Current Currency (IP Location)");
        endCurrency.getItems().addAll("USD", "EUR", "JPY", "INR", "CNH", "MXN", "CAD", "GBP",
            "SAR", "AUD", "CHF", "NZD", "HKD", "PKR", "BRL", "Current Currency (IP Location)");
        //endCurrency.setValue("Current Currency (IP Location)");
        endCurrency.setValue("Ending Currency");

        //endCurrency.getItems().addAll("USD", "EUR", "JPY", "INR", "CNH", "MXN", "CAD", "GBP",
        //"DKK", "NOK", "IRR", "SAR", "AUD", "CHF", "NZD", "HKD", "PKR", "BRL", "BTC");
        //endCurrency.setValue("USD");
        //root.setAlignment(Pos.CENTER);
        //title.setTextAlignment(TextAlignment.CENTER);
        //link.setTextAlignment(TextAlignment.CENTER);
        //content.setTextAlignment(TextAlignment.CENTER);
/*        title.setPrefWidth(500);
        title.setPrefHeight(5);
        title.setEditable(false);
        content.setPrefWidth(500);
        content.setPrefHeight(100);
        content.setEditable(false);
        link.setEditable(false);
        link.setPrefWidth(500);
        link.setPrefHeight(5);*/
        //hbox.setAlignment(Pos.CENTER);
        amount.setPrefWidth(320);
        //currency.setPrefWidth(200);
        //startingCurrency.setPrefWidth(200);
        //HBox.setHgrow(amount, Priority.ALWAYS);
        //HBox.setHgrow(currency, Priority.ALWAYS);
        //HBox.setHgrow(startingCurrency, Priority.ALWAYS);
        hbox.getChildren().addAll(amount, startCurrency, endCurrency, convert);
        bottom.getChildren().addAll(test, testIP, testButton);
        HBox.setHgrow(testIP, Priority.ALWAYS);
        root.getChildren().addAll(hbox, separator, result, separator2, warning, separator3, bottom);
        Runnable handler2 = () -> {
            convert.setDisable(true);
            testButton.setDisable(true);
            getLocation();
            getCurrency();
            convert.setDisable(false);
            testButton.setDisable(false);
        };
        Runnable handler3 = () -> {
            convert.setDisable(true);
            testButton.setDisable(true);
            testLocation();
            testCurrency();
            convert.setDisable(false);
            testButton.setDisable(false);
        };

        convert.setOnAction(event -> runInNewThread(handler2));
        testButton.setOnAction(event -> runInNewThread(handler3));

        System.out.println("init() called");
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;
        scene = new Scene(root);

        // setup stage
        stage.setTitle("Currency Converter");
        //stage.setResizable(true);
        //stage.setHeight(10000);
        //stage.setWidth(10000);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /** {@inheritDoc} */
    @Override
    public void stop() {
        // feel free to modify this method
        System.out.println("stop() called");
    } // stop

/*
    private void getIP() {
        try {
            String url = "https://1.1.1.1/cdn-cgi/trace";
            URI userIP = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(userIP).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String body = response.body();
            System.out.println(response.body());
            int start = body.indexOf("ip");
            int end = body.indexOf("ts");
            String ipAddress = body.substring(start + 3, end);
            ip = ipAddress;
            System.out.println(ipAddress);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.err.println(e);
        }

    }
*/
    /**
     * Using the users ip address, the Abstract API will return the currency code
     * information and store it in an instance variable to be used at a later time.
     */
    private void getLocation() {
        //System.out.println("TESTING");
        try {
            String api = "";
            String configPath = "resources/config.properties";
            try (FileInputStream configFileStream = new FileInputStream(configPath)) {
                Properties config = new Properties();
                config.load(configFileStream);
                api = config.getProperty("abstractapi.apikey");      // get project.year
                //System.out.printf("API = %s\n", api);
            } catch (IOException ioe) {
                System.err.println(ioe);
                ioe.printStackTrace();
            } // try
            //System.out.println("Normal API: " + api);
            //String api = "0f2debc2ed534b2e9e3fd73c2871154e";
            String url = "https://ipgeolocation.abstractapi.com/v1/?api_key=" + api;
            //System.out.println(url);
            TimeUnit.SECONDS.sleep(2);
            URI ipData = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(ipData).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String body = response.body();
            //System.out.println(body);
            IPData locationInfo = GSON.<IPData>fromJson(body, IPData.class);
            //System.out.println(locationInfo.country);
            region = locationInfo.regionIsoCode;
            country = locationInfo.countryCode;
            city = locationInfo.city;
            currencyCode = locationInfo.currency.currencyCode;
            //System.out.println(currencyCode);
            //System.out.println(region + " " + city + " " + country);

//        String url = ("https://ipgeolocation.abstractapi.com/v1/?" + api + "&" + ip);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater(() -> alertError(e));
            //System.err.println(e);
            //e.printStackTrace();
        }
    }

    /**
     * Allows the user to test the program with random IP addresses to get different
     * currency codes to be used in currency exchange calculations.
     */
    private void testLocation() {
        try {
            String api = "";
            String configPath = "resources/config.properties";
            try (FileInputStream configFileStream = new FileInputStream(configPath)) {
                Properties config = new Properties();
                config.load(configFileStream);
                api = config.getProperty("abstractapi2.apikey");      // get project.year
                //System.out.printf("API = %s\n", api);
            } catch (IOException ioe) {
                System.err.println(ioe);
                ioe.printStackTrace();
            } // try
            //System.out.println("Test API: " + api);
            String ipTest = "&ip_address=" + testIP.getText();
            String url = "https://ipgeolocation.abstractapi.com/v1/?api_key=" + api + ipTest;
            TimeUnit.SECONDS.sleep(2);
            URI ipData = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(ipData).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String body = response.body();
            //System.out.println(body);
            IPData locationInfo = GSON.<IPData>fromJson(body, IPData.class);
            //System.out.println(locationInfo.country);
            region = locationInfo.regionIsoCode;
            country = locationInfo.countryCode;
            city = locationInfo.city;
            testCurrencyCode = locationInfo.currency.currencyCode;
            //System.out.println(region + " " + city + " " + country);

//        String url = ("https://ipgeolocation.abstractapi.com/v1/?" + api + "&" + ip);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater(() -> alertError(e));
            //System.err.println(e);
            //e.printStackTrace();
        }
    }

/*
    /**
     * Using the city, region, and country information stored from the AbstractAPI
     * call, the NewsAPI is queried using the location infromation as parameters in
     * the query to find news articles about the users location.

    private void getNews() {
        try {
            String api = "&apiKey=";
            String configPath = "resources/config.properties";
            try (FileInputStream configFileStream = new FileInputStream(configPath)) {
                Properties config = new Properties();
                config.load(configFileStream);
                api += config.getProperty("newsapi.apikey");      // get project.year
                //System.out.printf("API = %s\n", api);
            } catch (IOException ioe) {
                System.err.println(ioe);
                ioe.printStackTrace();
            } // try
            //String api = "&apiKey=62eb4bca88324a83a569f74d3f65dbdd";
            String endpoint = "https://newsapi.org/v2/everything?q=";
            String url;
            if (country.equals("US")) {
                url = endpoint + city + ",%20" + region + api;
            } else {
                url = endpoint + city + ",%20" + country + api;
            }
            URI newsData = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(newsData).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String body = response.body();
            //System.out.println(body);
            NewsArticles news = GSON.<NewsArticles>fromJson(body, NewsArticles.class);
            int articleNumber = ((int)(Math.random() * news.articles.length));
            String newTitle = news.articles[articleNumber].title;
            title.setText(newTitle);
            String newContent = news.articles[articleNumber].description;
            content.setText(newContent);
            String newLink = news.articles[articleNumber].url;
            //link.setText(news.articles[articleNumber].url);
            link.setText(newLink);
            //System.out.println(newLink);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            alertError(e);
            //System.err.println(e);
            //e.printStackTrace();
        }
    }*/


    /**
     * Using the currency code information stored from the AbstractAPI
     * call, the ExchangeRate API is queried using the currency code infromation as parameters in
     * the query to find the rate between the specified currencies..
     */
    private void getCurrency() {
        try {
            if (Double.parseDouble(amount.getText()) < 0) {
                throw new IllegalArgumentException("Negative Currency Amount Entered");
            }
            String top = "Current Currency (IP Location)";
            if (startCurrency.getValue().equals(top) && endCurrency.getValue().equals(top)) {
                String startPoint = "https://api.exchangerate.host/convert?from=";
                String url = startPoint + currencyCode + "&to=" + currencyCode + "&places=4";
                URI currencyData = URI.create(url);
                HttpRequest request = HttpRequest.newBuilder().uri(currencyData).build();
                HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
                String body = response.body();
                CurrencyConvert data = GSON.<CurrencyConvert>fromJson(body, CurrencyConvert.class);
                if (data.info.rate == 0.0) {
                    throw new IllegalArgumentException("Illegal Currency Code Inputted");
                }
                double rate = data.info.rate;
                double finalAmount = rate * Double.parseDouble(amount.getText());
                String fAmount = "Converted Amount: " + finalAmount + " " + currencyCode;
                Platform.runLater(() -> result.setText(fAmount));
            } else if (startCurrency.getValue().equals(top)) {
                String tgt = endCurrency.getValue();
                String startPoint = "https://api.exchangerate.host/convert?from=";
                String url = startPoint + currencyCode + "&to=" + tgt + "&places=4";
                URI currencyData = URI.create(url);
                HttpRequest request = HttpRequest.newBuilder().uri(currencyData).build();
                HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
                String body = response.body();
                CurrencyConvert data = GSON.<CurrencyConvert>fromJson(body, CurrencyConvert.class);
                if (data.info.rate == 0.0) {
                    throw new IllegalArgumentException("Illegal Currency Code Inputted");
                }
                double rate = data.info.rate;
                double finalAmount = rate * Double.parseDouble(amount.getText());
                String fAmount = "" + finalAmount;
                Platform.runLater(() -> result.setText("Converted Amount: " + fAmount + " " + tgt));
            } else if (endCurrency.getValue().equals(top)) {
                String tgt = startCurrency.getValue();
                String startPoint = "https://api.exchangerate.host/convert?from=";
                String url = startPoint + tgt + "&to=" + currencyCode + "&places=4";
                URI currencyData = URI.create(url);
                HttpRequest request = HttpRequest.newBuilder().uri(currencyData).build();
                HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
                String body = response.body();
                CurrencyConvert data = GSON.<CurrencyConvert>fromJson(body, CurrencyConvert.class);
                if (data.info.rate == 0.0) {
                    throw new IllegalArgumentException("Illegal Currency Code Inputted");
                }
                double rate = data.info.rate;
                double finalAmount = rate * Double.parseDouble(amount.getText());
                String fAmount = "" + finalAmount + " " + currencyCode;
                Platform.runLater(() -> result.setText("Converted Amount: " + fAmount));
            } else {
                lastElseStatement();
            }
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater(() -> alertError(e));
        }
    }

    /**
     * Helper method for the getCurrency method so that the getCurrency method
     * does not run too long. Replaces the contents of the last else statement in the
     * getCurrency method.
     */
    private void lastElseStatement() {
        try {
            String tgt = endCurrency.getValue();
            String startPoint = "https://api.exchangerate.host/convert?from=";
            String start = startCurrency.getValue();
            String url = startPoint + start + "&to=" + tgt + "&places=4";
            URI currencyData = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(currencyData).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Don't Convert Too Quickly");
            } // if
            String body = response.body();
            CurrencyConvert data = GSON.<CurrencyConvert>fromJson(body, CurrencyConvert.class);
            if (data.info.rate == 0.0) {
                throw new IllegalArgumentException("Illegal Currency Code Inputted");
            }
            double rate = data.info.rate;
            double finalAmount = rate * Double.parseDouble(amount.getText());
            String fAmount = "" + finalAmount;
            Platform.runLater(() -> result.setText("Converted Amount: " + fAmount + " " + tgt));
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater(() -> alertError(e));
        }
    }

    /**
     * Uses the test IP inputted by the user as the starting currency to calculate
     * the currency exchange to the currency specified by the user.
     */
    private void testCurrency() {
        try {
            //System.out.println(testCurrencyCode);
            if (Double.parseDouble(amount.getText()) < 0) {
                throw new IllegalArgumentException("Negative Currency Amount Entered");
            }
            String tgt = "error";
            if (endCurrency.getValue().equals("Current Currency (IP Location)")) {
                getLocation();
                tgt = currencyCode;
            } else {
                tgt = endCurrency.getValue();
            }
            //System.out.println(tgt);
            String startPoint = "https://api.exchangerate.host/convert?from=";
            String start = testCurrencyCode;
            String url = startPoint + start + "&to=" + tgt + "&places=4";
            URI currencyData = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(currencyData).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Don't Convert Too Quickly");
            } // if
            String body = response.body();
            CurrencyConvert data = GSON.<CurrencyConvert>fromJson(body, CurrencyConvert.class);
            if (data.info.rate == 0.0) {
                throw new IllegalArgumentException("Illegal Currency Code Inputted");
            }
            double rate = data.info.rate;
            double finalAmount = rate * Double.parseDouble(amount.getText());
            String fAmount = "" + finalAmount + " " + tgt;
            Platform.runLater(() -> result.setText("Converted Amount: " + fAmount));
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            Platform.runLater(() -> alertError(e));
        }
    }

    /**
     * Show an error alert based on {@code error}.
     * @param error a {@link java.lang.Throwable Throwable} that caused the error
     */
    private void alertError(Throwable error) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setWidth(550);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(error.toString());
        errorAlert.showAndWait();
    } // alertError

    /**
     * Takes a runnable and puts it inside of a thread and starts it.
     *
     * @param task the Runnable to put in a thread and ran.
     */
    private void runInNewThread(Runnable task) {
        Thread t = new Thread(task);
        t.start();
    }

} // ApiApp
