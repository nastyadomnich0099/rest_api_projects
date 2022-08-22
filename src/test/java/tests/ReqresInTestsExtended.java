package tests;

import models.lombok.*;
import models.pojo.UserDataBody;
import models.pojo.UserDataResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static specifications.CreateUserSpec.requestUser;
import static specifications.CreateUserSpec.responseUser;
import static specifications.ListResourceSpec.request;
import static specifications.ListResourceSpec.response;
import static specifications.LoginFailedSpec.loginUnResponse;
import static specifications.LoginFailedSpec.loginUnSpec;
import static specifications.LoginSpec.loginResponseSpec;
import static specifications.LoginSpec.loginSpec;
import static specifications.PatchUserSpec.patchResponse;
import static specifications.PatchUserSpec.patchSpec;



public class ReqresInTestsExtended extends TestBase {


    @Test
    public void checkEmailUsingGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users?delay=3")
                .then()
                .spec(response)
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("janet.weaver@reqres.in"));
    }

    @Test
    @DisplayName("Create user with lombok and specs")
    public void createUser() {
        CreateUser user = new CreateUser();
        user.setName("morpheus");
        user.setJob("leader");
        CreateUserResponse userResponse = given()
                .spec(requestUser)
                .body(user)
                .when()
                .post("/users")
                .then()
                .spec(responseUser)
                .extract().as(CreateUserResponse.class);
        assertThat(userResponse.getJob()).isEqualTo("leader");
        assertThat(userResponse.getName()).isEqualTo("morpheus");

    }





    @Test
    @DisplayName("List Resource test with specs")
    public void listResource() {

        UserData data = given()
                .spec(request)
                .when()
                .get("/unknown")
                .then()
                .spec(response)
                .extract().as(UserData.class);
        assertThat(data.getPerPage()).isEqualTo(6);
        assertThat(data.getTotalPages()).isEqualTo(2);


    }


    @Test
    @DisplayName("Pojo Successful patch user test")
    public void patchUser() {
        UserDataBody userData = new UserDataBody();
        userData.setName("morpheus");
        userData.setJob("zion resident");

        UserDataResponse responseModel = given()
                .spec(patchSpec)
                .body(userData)
                .when()
                .patch()
                .then()
                .spec(patchResponse)
                .extract().as(UserDataResponse.class);

        assertThat(responseModel.getJob()).isEqualTo("zion resident");
        assertThat(responseModel.getName()).isEqualTo("morpheus");


    }

    @Test
    @DisplayName("Pojo unSuccessful login test")
    public void postLoginUnsuccessful() {
        UserDataBody userData = new UserDataBody();
        userData.setEmail("nana@ktimy");
        userData.setName("Hora");
        userData.setJob("Timy");

        UserDataResponse responseModel = given()
                .spec(loginUnSpec)
                .body(userData)
                .when()
                .post()
                .then()
                .spec(loginUnResponse)
                .extract().as(UserDataResponse.class);

        assertThat(responseModel.getError()).isEqualTo("Missing password");


    }

    @Test
    @DisplayName("Lombok login test")
    public void postLoginTest() {
        UserLoginBody loginBody = new UserLoginBody();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        UserLoginResponse loginResponse = given()
                .spec(loginSpec)
                .body(loginBody)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().as(UserLoginResponse.class);

        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");


    }
}