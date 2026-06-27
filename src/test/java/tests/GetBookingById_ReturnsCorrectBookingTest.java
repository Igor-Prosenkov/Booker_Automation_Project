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
                .getObject("bookingDates", BookingDates.class);
assertThat(response1.getStatusCode()).isEqualTo(200);
assertThat(response1.jsonPath().getString("firstName")).isEqualTo("Ivan");
assertThat(response.jsonPath().getString("additionalNeeds")).isEqualTo("Breakfast");
assertThat(response.jsonPath().getInt("totalPrice")).isEqualTo(111);
assertThat(response.jsonPath().getBoolean("depositPaid")).isEqualTo(true);
        assertThat(response.jsonPath().getString("lastName")).isEqualTo("Pyotrov");
        assertThat(actualDates.getCheckin()).isEqualTo("2026-06-27");
        assertThat(actualDates.getCheckout()).isEqualTo("2026-06-30");
    }

    @AfterEach
    public void verifyDeletedSuccessfully() {
        Response deleteCreatedBooking = apiClients.deleteBooking(createdBookingId);
        assertThat(deleteCreatedBooking.getStatusCode()).isEqualTo(201);
    }
}
