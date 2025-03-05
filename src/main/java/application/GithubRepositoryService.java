package application;

import domain.GithubRepo;
import domain.RepositoryService;
import infrastructure.rest.github.GithubDataService;
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
    public List<GithubRepo> getUserRepository(String username) {
        final List<GithubRepo> response = githubDataService.getUserRepository(username);

        if (response.isEmpty()) {
            return Collections.emptyList();
        }

        return response.stream().filter(repo -> !repo.isFork()).toList();
    }
}
