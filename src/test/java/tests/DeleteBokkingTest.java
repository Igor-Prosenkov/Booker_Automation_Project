package tests;

import core.clients.APIClients;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DeleteBokkingTest {
    private APIClients apiClients;
    private String token;

    @BeforeEach
    public void setup() {
        apiClients = new APIClients();
        apiClients.createToken("admin","password123");
        token = apiClients.getToken();
    }

    @Test
    public void testDeleteBooking() {
        GetBookingTest existingTest = new GetBookingTest();
        existingTest.testGetBooking(); //запускаем существующий тест

        List<Booking> bookings = existingTest.bookings;
        int bookingId = bookings.get(0).getBookingid();

        Response response = apiClients.deleteBooking(bookingId);

        assertThat(response.getStatusCode()).isEqualTo(201);
    }
}
