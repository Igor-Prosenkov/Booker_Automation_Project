package tests;

import core.clients.APIClients;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {

    // Это поле нужно для передачи авторизованного клиента из другого теста
    public APIClients apiClients;

    // --- ВАШ СУЩЕСТВУЮЩИЙ ТЕСТ (немного упрощен) ---
    @Test
    public void testGetBooking() throws Exception {
        // Для независимого запуска теста создаем своего клиента
        APIClients localApiClient = new APIClients();
        localApiClient.createToken("admin", "password123");

        Response response = localApiClient.getBooking();

        assertThat(response.getStatusCode()).isEqualTo(200);

        // Используем встроенную десериализацию RestAssured, это проще
        List<Booking> bookings = response.jsonPath().getList(".", Booking.class);

        assertThat(bookings).isNotEmpty();
    }
    // ------------------------------------------------

    /**
     * Служебный метод для получения списка броней.
     * Он использует уже существующий, авторизованный экземпляр APIClients.
     * @return Список объектов Booking или пустой список, если ничего не найдено.
     */
    public List<Booking> getAllBookings() {
        // Проверяем, что клиент был передан из внешнего теста
        if (this.apiClients == null) {
            throw new IllegalStateException("API client is not initialized. Call setup before using this method.");
        }

        Response response = this.apiClients.getBooking();

        // Проверяем успешность ответа. Если код не 200, тест упадет здесь.
        assertThat(response.getStatusCode())
                .as("Запрос на получение списка броней завершился с ошибкой")
                .isEqualTo(200);

        // Десериализуем JSON-ответ напрямую в список объектов Booking.
        // Если API вернет не массив, а объект с массивом внутри (например, { "data: [...] }),
        // нужно указать путь: response.jsonPath().getList("data", Booking.class);
        List<Booking> bookings = response.jsonPath().getList(".", Booking.class);

        return bookings;
    }
}