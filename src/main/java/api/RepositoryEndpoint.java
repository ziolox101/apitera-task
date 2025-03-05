package api;

import application.GithubRepositoryService;
import domain.Branch;
import domain.GithubRepo;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Path("/repos")
@Produces(MediaType.APPLICATION_JSON)
public class RepositoryEndpoint {

    private final GithubRepositoryService githubRepositoryService;

    @Inject
    public RepositoryEndpoint(GithubRepositoryService githubRepositoryService) {
        this.githubRepositoryService = githubRepositoryService;
    }

    @GET
    @Path("/{username}")
    @Blocking
    public Uni<List<RepoResponse>> repos(@PathParam("username") String username) {
        final List<GithubRepo> result = githubRepositoryService.getUserRepository(username);
        final List<RepoResponse> repoResponses = Mapper.toRepoResponseList(result);
        return Uni.createFrom().item(repoResponses);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    private static class RepoResponse {
        private String repositoryName;
        private String ownerLogin;
        private List<BranchResponse> branches;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    private static class BranchResponse {
        private String name;
        private String commitSha;
    }

    private static class Mapper {

        public static List<RepoResponse> toRepoResponseList(List<GithubRepo> repos) {
            if (repos.isEmpty()) {
                return Collections.emptyList();
            }
            return repos.stream()
                    .map(Mapper::toRepoResponse)
                    .toList();
        }

        private static RepoResponse toRepoResponse(GithubRepo repo) {
            return new RepoResponse(
                    repo.getRepositoryName(),
                    repo.getOwnerLogin(),
                    toBranchResponseList(repo.getBranches())
            );
        }

        private static BranchResponse toBranchResponse(Branch branch) {
            return new BranchResponse(branch.getName(), branch.getCommitSha());
        }

        private static List<BranchResponse> toBranchResponseList(List<Branch> branches) {
            return branches.stream()
                    .map(Mapper::toBranchResponse)
                    .toList();
        }
    }

}
