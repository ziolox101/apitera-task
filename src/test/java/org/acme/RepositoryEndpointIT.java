package org.acme;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;


@QuarkusTest
class RepositoryEndpointIT {
    private static final WireMockServer mockGithubApiServer = new WireMockServer(5555);

    @BeforeEach
    void beforeEach() {
        mockGithubApiServer.resetAll();
        mockGithubApiServer.start();
    }

    @AfterEach
    void afterEach() {
        mockGithubApiServer.resetAll();
    }

    @AfterAll
    static void afterAll() {
        mockGithubApiServer.shutdownServer();
    }

    @Test
    void shouldGetGithubRepositoryResponse() {
        //given
        final String username = "user1234";
        prepareStubs();

        //when
        final Response response = given().get("/repos/%s".formatted(username));

        //then
        response.then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].repositoryName", Matchers.equalTo("android"))
                .body("[0].ownerLogin", Matchers.equalTo("user1234"))
                .body("[0].branches", hasSize(2))
                .body("[0].branches[0].name", Matchers.equalTo("master"))
                .body("[0].branches[0].commitSha", Matchers.equalTo("4e6745a0b84e8fde3b0f7d8c6295d51b5e1fffb1"))
                .body("[0].branches[1].name", Matchers.equalTo("test-fail-build"))
                .body("[0].branches[1].commitSha", Matchers.equalTo("64cd053955b8056ed8e3a8e74532ca3f5fb8ef13"));
    }


    private void prepareStubs() {
        mockGithubApiServer.stubFor(
                get(urlEqualTo("/users/user1234/repos"))
                        .withHeader("Authorization", equalTo("Bearer eqwsdadsad"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(GithubMockResponses.getUserRepos())));

        mockGithubApiServer.stubFor(
                get(urlEqualTo("/repos/user1234/clients/branches"))
                        .withHeader("Authorization", equalTo("Bearer eqwsdadsad"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(GithubMockResponses.getRepoBranches())));

        mockGithubApiServer.stubFor(
                get(urlEqualTo("/repos/user1234/android/branches"))
                        .withHeader("Authorization", equalTo("Bearer eqwsdadsad"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(GithubMockResponses.getRepoBranches())));
    }
}