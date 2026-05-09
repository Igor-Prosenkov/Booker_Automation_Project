package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClients;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class getbookoingByIdTest {
        private APIClients apiClients;
        private ObjectMapper objectMapper;

        @BeforeEach
    public void setup() {
            apiClients = new APIClients();
        }

        @Test
    public void TestGetBookingId() throws IOException {
            Response response =apiClients.getbookoingById(1);
            objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            assertThat(response.getStatusCode()).isEqualTo(200);
            ObjectMapper objectMapper = new ObjectMapper();

            Booking booking = objectMapper.readValue(response.body().asString(), Booking.class);
            assertThat(booking.getFirstname()).isNotBlank();
            assertThat(booking.getLastname()).isNotBlank();
            assertThat(booking.getTotalprice()).isPositive();
            assertThat(booking.isDepositpaid()).isNotNull();
            assertThat(booking.getBookingdates().getCheckin()).isNotBlank();
            assertThat(booking.getBookingdates().getCheckout()).isNotBlank();
            assertThat(booking.getAdditionalneeds()).isNotNull();
            //вот тут не придумал что лучше прописать, хочу условие что просто просто есть наличие строки и не важно ято она содержит




            





        }
}


