package np.com.oskarshrestha.loginregistration.apitesting;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestAssuredTesting {
    @Test
    public void whenNewUserRegisterRequest_returnRegistrationResponseRegistrationSuccessTrue(){
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("firstName", "hi");
        registrationRequest.put("lastName", "Shrestha");
        registrationRequest.put("email", "hi@gmail.com");
        registrationRequest.put("password", "123");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registrationRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/register")
                .when()
                .post();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertTrue((Boolean) responseObject.get("registrationSuccess"));
        assertFalse((Boolean) responseObject.get("existingUser"));
    }

    @Test
    public void whenExistingUserRegisterRequest_returnRegistrationResponseExistingUserTrue(){
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("firstName", "hi");
        registrationRequest.put("lastName", "Shrestha");
        registrationRequest.put("email", "hi@gmail.com");
        registrationRequest.put("password", "123");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registrationRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/register")
                .when()
                .post();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertFalse((Boolean) responseObject.get("registrationSuccess"));
        assertTrue((Boolean) responseObject.get("existingUser"));
    }

    @Test
    public void whenTokenIsValid_returnVerifyEmailResponseWithEmailVerificationTokenStatusValid(){
        String token = "d1cb13e1-d6a1-4e54-8f8a-e92749ea89e2";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/verifyEmail")
                .queryParam("token",token)
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("VALID",responseObject.get("emailVerificationTokenStatus"));
    }

    @Test
    public void whenTokenIsInvalid_returnVerifyEmailResponseWithEmailVerificationTokenStatusInvalid(){
        String token = "some-invalid-token";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/verifyEmail")
                .queryParam("token",token)
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("INVALID",responseObject.get("emailVerificationTokenStatus"));
    }

    @Test
    public void whenValidAuthenticateRequest_returnAuthenticationResponseWithToken(){
        JSONObject authenticationRequest = new JSONObject();
        authenticationRequest.put("email", "hi@gmail.com");
        authenticationRequest.put("password", "123");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/authenticate")
                .when()
                .post();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("valid-authentication-token",responseObject.get("token"));
    }

    @Test
    public void whenInvalidAuthenticateRequest_returnAuthenticationResponseWithTokenAsNotValid(){
        JSONObject authenticationRequest = new JSONObject();
        authenticationRequest.put("email", "hi@gmail.com");
        authenticationRequest.put("password", "123");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/authenticate")
                .when()
                .post();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("not-valid",responseObject.get("token"));
    }

    @Test
    public void whenValidForgetPasswordRequest_returnForgetPasswordResponseWithForgetPasswordEmailStatusSent(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/forgetPassword")
                .param("email","hi@gmail.com")
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("SENT",responseObject.get("forgetPasswordEmailStatus"));
    }

    @Test
    public void whenInvalidForgetPasswordRequest_returnForgetPasswordResponseWithForgetPasswordEmailStatusEmailNotFound(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/forgetPassword")
                .param("email","hello@gmail.com")
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("EMAIL_NOT_FOUND",responseObject.get("forgetPasswordEmailStatus"));
    }

    @Test
    public void whenValidChangePasswordRequest_returnChangePasswordResponseWithChangeUserPasswordStatusSuccess(){
        JSONObject changePasswordRequest = new JSONObject();
        changePasswordRequest.put("email", "hi@gmail.com");
        changePasswordRequest.put("oldPassword", "123");
        changePasswordRequest.put("newPassword", "456");

        Response response = RestAssured.given()
                .headers(
                        "Authorization",
                        "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBnbWFpbC5jb20iLCJpYXQiOjE2ODYyMjU0MzIsImV4cCI6MTY4NjIyNjg3Mn0.qV7UHvQZWOK8SbXDgHNTTkQ65Vw43_OgyI0yUmVoiFM" ,
                        "Content-Type",
                        ContentType.JSON)
                .body(changePasswordRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/changePassword")
                .when()
                .post();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("SUCCESS", responseObject.get("changeUserPasswordStatus"));
    }

    @Test
    public void whenInvalidAuthTokenChangePasswordRequest_returnAccessForbidden(){
        JSONObject changePasswordRequest = new JSONObject();
        changePasswordRequest.put("email", "hi@gmail.com");
        changePasswordRequest.put("oldPassword", "123");
        changePasswordRequest.put("newPassword", "456");

        Response response = RestAssured.given()
                .headers(
                        "Authorization",
                        "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBnbWFpbC5jb20iLCJpYXQiOjE2ODYyMjU0MzIsImV4cCI6MTY4NjIyNjg3Mn0.qV7UHvQZWOK8SbXDgHNTTkQ65Vw43_OgyI0yUmVoiFM" ,
                        "Content-Type",
                        ContentType.JSON)
                .body(changePasswordRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/changePassword")
                .when()
                .post();

        // assertions
        response.then().assertThat().statusCode(403);
    }

    @Test
    public void whenInvalidEmailChangePasswordRequest_returnChangePasswordResponseWithChangeUserPasswordStatusEmailNotFound(){
        JSONObject changePasswordRequest = new JSONObject();
        changePasswordRequest.put("email", "hello@gmail.com");
        changePasswordRequest.put("oldPassword", "123");
        changePasswordRequest.put("newPassword", "456");

        Response response = RestAssured.given()
                .headers(
                        "Authorization",
                        "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBnbWFpbC5jb20iLCJpYXQiOjE2ODYyMjU0MzIsImV4cCI6MTY4NjIyNjg3Mn0.qV7UHvQZWOK8SbXDgHNTTkQ65Vw43_OgyI0yUmVoiFM" ,
                        "Content-Type",
                        ContentType.JSON)
                .body(changePasswordRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/changePassword")
                .when()
                .post();

        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("EMAIL_NOT_FOUND", responseObject.get("changeUserPasswordStatus"));
    }

    @Test
    public void whenInvalidOldPasswordChangePasswordRequest_returnChangePasswordResponseWithChangeUserPasswordStatusPasswordMismatch(){
        JSONObject changePasswordRequest = new JSONObject();
        changePasswordRequest.put("email", "hello@gmail.com");
        changePasswordRequest.put("oldPassword", "123");
        changePasswordRequest.put("newPassword", "456");

        Response response = RestAssured.given()
                .headers(
                        "Authorization",
                        "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlckBnbWFpbC5jb20iLCJpYXQiOjE2ODYyMjU0MzIsImV4cCI6MTY4NjIyNjg3Mn0.qV7UHvQZWOK8SbXDgHNTTkQ65Vw43_OgyI0yUmVoiFM" ,
                        "Content-Type",
                        ContentType.JSON)
                .body(changePasswordRequest.toString())
                .baseUri("http://localhost:8080/api/v1/auth/changePassword")
                .when()
                .post();

        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("PASSWORD_MISMATCH", responseObject.get("changeUserPasswordStatus"));
    }

    @Test
    public void whenValidResendForgetPasswordRequest_returnResendForgetPasswordEmailResponseWithResendForgetPasswordEmailStatusSuccess(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/resendEmailVerification")
                .param("email","hi@gmail.com")
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("SUCCESS",responseObject.get("resendVerifyEmailStatus"));
    }

    @Test
    public void whenInvalidEmailResendForgetPasswordRequest_returnResendForgetPasswordEmailResponseWithResendForgetPasswordEmailStatusEmailNotRegistered(){
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost:8080/api/v1/auth/resendEmailVerification")
                .param("email","hello@gmail.com")
                .when()
                .get();
        JSONObject responseObject =  response.getBody().as(JSONObject.class);

        // assertions
        response.then().assertThat().statusCode(200);
        assertEquals("EMAIL_NOT_REGISTERED",responseObject.get("resendVerifyEmailStatus"));
    }

}
