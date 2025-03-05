package infrastructure.rest.github;


import infrastructure.exception.ExternalServiceExceptionMapper;
import infrastructure.rest.github.response.GithubRepoBranchResponse;
import infrastructure.rest.github.response.GithubRepoResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "github-rest-client")
@RegisterProvider(ExternalServiceExceptionMapper.class)
@ClientHeaderParam(name = "Authorization", value = "Bearer ${github.token}")
interface GithubRestClient {

    @GET
    @Path("/users/{username}/repos")
    Uni<List<GithubRepoResponse>> getUserRepos(@PathParam("username") String username);

    @GET
    @Path("/repos/{username}/{repo}/branches")
    Uni<List<GithubRepoBranchResponse>> getUserRepoBranches(@PathParam("username") final String username, @PathParam("repo") final String repo);
}
