package infrastructure.rest.github;

import domain.Branch;
import domain.GithubRepo;
import infrastructure.rest.github.response.GithubRepoBranchResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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

    public Uni<List<GithubRepo>> getUserRepository(String username) {
        return githubRestClient.getUserRepos(username)
                .onItem().transformToMulti(repos -> {
                    if (repos.isEmpty()) {
                        return Multi.createFrom().empty();
                    }
                    return Multi.createFrom().iterable(repos);
                })
                .onItem().transformToUniAndMerge(repo -> githubRestClient.getUserRepoBranches(username, repo.getName())
                        .onItem().transform(branches -> new GithubRepo(
                                repo.getName(),
                                repo.getOwner().getLogin(),
                                repo.isFork(),
                                map(branches)
                        )))
                .collect().asList()
                .onFailure().recoverWithItem(Collections.emptyList());
    }

    private List<Branch> map(final List<GithubRepoBranchResponse> response) {
        return response.stream().map(this::map).toList();
    }

    private Branch map(final GithubRepoBranchResponse response) {
        return new Branch(response.getName(), response.getCommit().getSha());
    }
}
