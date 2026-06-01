package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClients;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class    GetBookingTest {
    public List<Booking> bookings;
    private APIClients apiClients;
    private ObjectMapper objectMapper;

    @Test
    public void testGetBooking() throws Exception {
        apiClients = new APIClients();
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Response response = apiClients.getBooking();

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody,new TypeReference<List<Booking>>()
        {}
        );

        assertThat(bookings).isNotEmpty();

        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }
    }
}
