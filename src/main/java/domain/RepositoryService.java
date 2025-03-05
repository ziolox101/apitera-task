package domain;

import java.util.List;

public interface RepositoryService {

    List<GithubRepo> getUserRepository(final String username);
}
