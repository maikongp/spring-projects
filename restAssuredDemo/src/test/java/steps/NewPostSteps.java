package steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import static io.restassured.config.EncoderConfig.encoderConfig;

public class NewPostSteps {

    RequestSpecification request;
    private static Response response;
    private int status_code;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost/";
        RestAssured.port = 3000;
        request = RestAssured.given();
        request.header("Content-type", "application-json")
                .config(RestAssured.config().encoderConfig(
                        encoderConfig().encodeContentTypeAs("application-json", 
                                ContentType.JSON)));
    }

    @Given("Provide the values {string}{string}")
    public void provideTheValues(String title, String author) {
        request.body("\"title\": \"" + title + "\", \"author\":\"" + author + "\"");
    }

    @When("Send a Post Request")
    public void sendAPostRequest() {
        response = request.post("/posts");
    }

    @Then("Status_code is equal to {int}")
    public void status_codeIsEqualsToStatus_code(int status_code) {
        Assert.assertEquals(status_code, response.getStatusCode());
    }
}
