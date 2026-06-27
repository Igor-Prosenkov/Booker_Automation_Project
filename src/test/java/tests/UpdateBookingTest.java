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

public class UpdateBookingTest {
    private APIClients apiClients;
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
    @Step("Обновляю бронирование")
    public void updateBooking(){
        apiClients = new APIClients();
        apiClients.createToken("admin", "password123");
        BookingDates dates = new BookingDates("2026-06-28", "2026-06-30");
        CreateBookingRequest newBooking = new CreateBookingRequest(
                "Ivan", "Pytrov", 22, true, dates, "Breakfast");
        Response response = apiClients.updateBooking(createdBookingId, newBooking);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getInt("totalprice")).isEqualTo(22);
        assertThat(response.jsonPath().getString("lastname")).isEqualTo("Pytrov");

    }

    @AfterEach
    public void verifyDeletedSuccessfully() {
        Response deleteCreatedBooking = apiClients.deleteBooking(createdBookingId);
        assertThat(deleteCreatedBooking.getStatusCode()).isEqualTo(201);
    }
}
