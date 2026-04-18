package tests;

import core.clients.APIClients;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

public class GetBookingTest {

    @Test
    public void testGetBooking() throws Exception {
        Response response = apiClients.getBooking();

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody,new TypeReference<List<Booking()>>);

        assertThat(bookings).isNotEmpty();

        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isCreaterThan(0);
        }
    }
}
