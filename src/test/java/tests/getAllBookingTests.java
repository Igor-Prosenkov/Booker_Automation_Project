import core.clients.APIClients;
import core.models.BookingDates;
import core.models.CreateBookingRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class getAllBookingTests {
    private APIClients apiClients;
    private int createdBookingId;

    @BeforeEach
    public void setup() {
        apiClients = new APIClients();
        apiClients.createToken("admin", "password123");
        BookingDates dates = new BookingDates("2026-06-18", "2026-06-20");
        CreateBookingRequest newBooking = new CreateBookingRequest
                ("Ivan", "Pyotrov", 111, true, dates, "Breakfast");
        Response response = apiClients.createBooking(newBooking);
        assertThat(response.getStatusCode()).isEqualTo(200);
        createdBookingId = response.jsonPath().getInt("bookingid");
    }

    @Test
    public void verifyCreatedBookingAppearsInGetAll() {
        Response listResonance = apiClients.getBooking();
        List<Integer> bookingIds = listResonance.jsonPath().getList("bookingid");
        assertThat(bookingIds).contains(createdBookingId);
    }

    @AfterEach
    public void verifyDeletedSuccessfully() {
        Response deleteCreatedBooking = apiClients.deleteBooking(createdBookingId);
        assertThat(deleteCreatedBooking.getStatusCode()).isEqualTo(201);
    }
}