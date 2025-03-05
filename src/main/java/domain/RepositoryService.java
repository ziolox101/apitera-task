package domain;

import io.smallrye.mutiny.Uni;

import java.util.List;

@FunctionalInterface
public interface RepositoryService {

    Uni<List<GithubRepo>> getUserRepository(final String username);
}
