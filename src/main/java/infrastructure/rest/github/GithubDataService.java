package infrastructure.rest.github;

import domain.Branch;
import domain.GithubRepo;
import infrastructure.rest.github.response.GithubRepoBranchResponse;
import infrastructure.rest.github.response.GithubRepoResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GithubDataService {

    private final GithubRestClient githubRestClient;

    @Inject
    public GithubDataService(@RestClient GithubRestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    public List<GithubRepo> getUserRepository(String username) {
        final List<GithubRepoResponse> response = githubRestClient.getUserRepos(username);

        if (response.isEmpty()) {
            return Collections.emptyList();
        }

        return response.stream()
                .map(repo -> {
                    final List<GithubRepoBranchResponse> branchResponse = githubRestClient.getUserRepoBranches(username, repo.getName());
                    return new GithubRepo(repo.getName(), repo.getOwner().getLogin(), repo.isFork(), map(branchResponse));
                })
                .toList();
    }

    private List<Branch> map(final List<GithubRepoBranchResponse> response) {
        return response.stream().map(this::map).toList();
    }

    private Branch map(final GithubRepoBranchResponse response) {
        return new Branch(response.getName(), response.getCommit().getSha());
    }
}
