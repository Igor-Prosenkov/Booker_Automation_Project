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
        apiClients.createToken("admin", "password123");
        token = apiClients.getToken();
    }

    @Test
    public void testDeleteBooking() {
        GetBookingTest existingTest = new GetBookingTest();
// Передаем в него нашего авторизованного клиента
        existingTest.apiClients = this.apiClients;

        try {
            // Вместо обращения к полю, ВЫЗЫВАЕМ МЕТОД
            List<Booking> bookings = existingTest.getAllBookings(); // Теперь мы вызываем метод!

            // Проверка на случай, если список все-таки окажется пустым
            if (bookings == null || bookings.isEmpty()) {
                throw new AssertionError("Список броней пуст. Нечего удалять.");
            }

            int bookingId = bookings.get(0).getBookingid();

            Response response = apiClients.deleteBooking(bookingId);
            assertThat(response.getStatusCode()).isEqualTo(201);

        } catch (AssertionError e) {
            // Обрабатываем ошибки из getAllBookings()
            throw new AssertionError("Не удалось получить или обработать список броней.", e);
        } catch (Exception e) {
            // Обрабатываем любые другие непредвиденные ошибки
            throw new AssertionError("Произошла непредвиденная ошибка: " + e.getMessage(), e);

        }
    }
}