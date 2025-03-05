package domain;

import java.util.List;

@FunctionalInterface
public interface RepositoryService {

    List<GithubRepo> getUserRepository(final String username);
}
