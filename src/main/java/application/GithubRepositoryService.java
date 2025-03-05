package application;

import domain.GithubRepo;
import domain.RepositoryService;
import infrastructure.rest.github.GithubDataService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GithubRepositoryService implements RepositoryService {

    private final GithubDataService githubDataService;

    @Inject
    public GithubRepositoryService(GithubDataService githubDataService) {
        this.githubDataService = githubDataService;
    }

    @Override
    public Uni<List<GithubRepo>> getUserRepository(String username) {
        return githubDataService.getUserRepository(username)
                .onItem().transform(repos -> repos.stream()
                        .filter(repo -> !repo.isFork())
                        .toList())
                .onFailure().recoverWithItem(Collections.emptyList());
    }
}
