package com.github.luecy1.androidstudyapp;

import com.github.luecy1.androidstudyapp.vo.Contributor;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2018/02/10.
 */
public class TestUtil {

    public static User createUser(String login) {
        return new User(login, null,
                login + " name" , null, null, null);
    }

    public static List<Repo> createRepos(int count, String owner, String name,
                                         String description) {
        List<Repo> repos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repos.add(createRepo(owner + 1, name + 1, description + 1));
        }
        return repos;
    }

    public static Repo createRepo(String owner, String name, String description) {
        return createRepo(Repo.UNKNOWN_ID, owner, name, description);
    }

    public static Repo createRepo(int id, String owner, String name, String description) {
        return new Repo(id, name, owner + "/" + name,
                description, new Repo.Owner(owner, null), 3);
    }

    public static Contributor createContributor(Repo repo, String login, int contributions) {
        Contributor contributor = new Contributor(login, contributions, null);
        contributor.setRepoName(repo.name);
        contributor.setRepoOwner(repo.owner.login);
        return contributor;
    }
}
