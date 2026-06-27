package core.clients;
import core.models.BookingDates;
import core.models.CreateBookingRequest;
import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.restassured.filter.Filter;


public class APIClients {

    private final String baseUrl;
    private String token;

    public String getToken(){
        return token;
    }

    public APIClients() {
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Не найден файл конфигурации" + configFileName);

            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Не найден файл конфигурации" + configFileName, e);
        }

        return properties.getProperty("baseUrl");
    }

    //Фильтр для добавления токена в заголовок авторизации
    public Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responceSpec,
                FilterContext ctx) -> {
            if (token != null) {
                requestSpec.header("Cookie","token=" + token);
            }
            return ctx.next(requestSpec, responceSpec);
        };

    }

    //Настройка для отправки параметров
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type","application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());

    }

    //метод для получения токена
    public void createToken(String username, String password) {
        // Тело для получения токена
        String requestBody = String.format("{ \"username\": \"%s\", \"password\": \"%s\"}", username,password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath())// надо использовать ENUM для эндпоинта /auth
                .then()
                .statusCode(200)
                .extract()
                .response();

        //извлекаем токен из ответа
        token = response.jsonPath().getString("token");
    }



    //get для ping
    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath())
                .then()
                .statusCode(201)
                .extract()
                .response();
    }


    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath()) // Используем ENUM для эндпоинта /booking
                .then()
                .statusCode(200) // Ожидаемый статус-код 200 OK
                .extract()
                .response();
    }

    public Response getBookingById (int id) {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response deleteBooking(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId) //указываем path параметр для ID
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() + "/{id}") // используем параметр пути в запросе
                .then()
                .log().all()
                .statusCode(201) //код ответа
                .extract()
                .response();
    }

    public Response createBooking(CreateBookingRequest request) {
        return getRequestSpec()
                .body(request)
                .when()
                .post(ApiEndpoints.BOOKING.getPath());

    }

    public Response updateBooking(int bookingId,CreateBookingRequest request){
        return getRequestSpec()
                .pathParam("id", bookingId)
                .body(request)
                .when()
                .put(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
    }

}