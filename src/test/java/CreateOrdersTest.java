import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    private static final String CREATE_ORDER = "/api/v1/orders";
    private static final String SCOOTER_URI = "https://qa-scooter.praktikum-services.ru/";

    private RequestSpecification requestSpecification;
    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public String firstName;
    public String lastName;
    public String address;
    public int metroStation;
    public String phone;
    public int rentTime;
    public String deliveryDate;
    public String comment;
    public String[] color;

    public CreateOrdersTest( String firstName, String lastName,
                            String address, int metroStation, String phone, int rentTime, String deliveryDate,
                            String comment, String[] color) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Создание заказа.Параметры.Тестовые данные: {0} {1} {2} {3} {4} {5} {6} {7} {8}")
        public static Object[][] CreateOrderPar() {
            return new Object[][]{
                    {"Елена", "Иванова", "Москва", 5, "89222222222", 4, "10.09.2023", "Комментарий ", new String[]{"BLACK"}},
                    {"Елена", "Иванова", "Москва", 5, "89222222222", 4, "10.09.2023", "Комментарий ", new String[]{"CREY"}},
                    {"Елена", "Иванова", "Москва", 5, "89222222222", 4, "10.09.2023", "Комментарий ", new String[]{"BLACK", "GREY"}},
                    {"Елена", "Иванова", "Москва", 5, "89222222222", 4, "10.09.2023", "Комментарий ", new String[]{}},

            };
        }

        @Test
        public void createOrderCode201_test () {

            requestSpecification =
                    new RequestSpecBuilder().setBaseUri(SCOOTER_URI)
                            .setContentType(ContentType.JSON)
                            .build();
            setRequestSpecification(requestSpecification);
            Order order = new Order(firstName,lastName,address, metroStation,
                    phone, rentTime, deliveryDate,comment,color);

            ValidatableResponse response =
                    given()
                    .spec(requestSpecification)
                    .header("Content-type", "application/json")
                    .body(order)
                    .post(CREATE_ORDER)
                    .then()
                    .log()
                    .all();
            response.assertThat().statusCode(201);
        }

    @Test
    public void createOrderContainsTrack_test () {

        requestSpecification =
                new RequestSpecBuilder().setBaseUri(SCOOTER_URI)
                        .setContentType(ContentType.JSON)
                        .build();
        setRequestSpecification(requestSpecification);
        Order order = new Order(firstName,lastName,address, metroStation,
                phone, rentTime, deliveryDate,comment,color);

        ValidatableResponse response =
                given()
                        .spec(requestSpecification)
                        .header("Content-type", "application/json")
                        .body(order)
                        .post(CREATE_ORDER)
                        .then()
                        .log()
                        .all();
        assertNotNull(response.extract().body().jsonPath().get("track"));
    }
}
