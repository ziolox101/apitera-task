package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GithubRepo {
    private String repositoryName;
    private String ownerLogin;
    private boolean fork;
    private List<Branch> branches;
}
