package tests;


import core.clients.APIClients;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



public class HeathCheckTests {
    private APIClients apiClients;

    @BeforeEach
    public void setup() {
    apiClients = new APIClients();
    }

    @Test
    public  void testPing() {
        Response response = apiClients.ping();
        assertThat(response.getStatusCode()).isEqualTo(201);
    }
}
