package tests;

import core.clients.APIClients;
import core.models.BookingDates;
import core.models.CreateBookingRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingById_ReturnsCorrectBookingTest {
    private APIClients  apiClients;
    private int createdBookingId;

    @BeforeEach
    @Step("Создаю бронирование")
    public void setup() {
        apiClients = new APIClients();
        apiClients.createToken("admin", "password123");
        BookingDates dates = new BookingDates("2026-06-27", "2026-06-30");
        CreateBookingRequest newBooking = new CreateBookingRequest(
                "Ivan", "Pyotrov", 111, true, dates, "Breakfast");
        Response response = apiClients.createBooking(newBooking);
        assertThat(response.getStatusCode()).isEqualTo(200);
        createdBookingId = response.jsonPath().getInt("bookingid");
    }
@Test
        @Step("Проверяю, что созданный id существует")
public void ReturnsCorrectBooking(){
 Response response1 = apiClients.getBookingById(createdBookingId);
    System.out.println("Статус чтения: " + response1.getStatusCode());
    System.out.println("Тело ответа: " + response1.getBody().asPrettyString());
        BookingDates actualDates = response1.jsonPath()
                .getObject("bookingdates", BookingDates.class);
assertThat(response1.getStatusCode()).isEqualTo(200);
assertThat(response1.jsonPath().getString("firstname")).isEqualTo("Ivan");
assertThat(response1.jsonPath().getString("additionalneeds")).isEqualTo("Breakfast");
assertThat(response1.jsonPath().getInt("totalprice")).isEqualTo(111);
assertThat(response1.jsonPath().getBoolean("depositpaid")).isEqualTo(true);
        assertThat(response1.jsonPath().getString("lastname")).isEqualTo("Pyotrov");
        assertThat(actualDates.getCheckin()).isEqualTo("2026-06-27");
        assertThat(actualDates.getCheckout()).isEqualTo("2026-06-30");
    }

    @AfterEach
    public void verifyDeletedSuccessfully() {
        Response deleteCreatedBooking = apiClients.deleteBooking(createdBookingId);
        assertThat(deleteCreatedBooking.getStatusCode()).isEqualTo(201);
    }
}
