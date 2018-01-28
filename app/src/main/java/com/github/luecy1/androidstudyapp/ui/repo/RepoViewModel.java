package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.luecy1.androidstudyapp.repository.RepoRepository;
import com.github.luecy1.androidstudyapp.util.AbsentLiveData;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import javax.inject.Inject;


/**
 * Created by you on 2018/01/28.
 */

public class RepoViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<RepoId> repoId;
    private final LiveData<Resource<Repo>> repo;
//    private final LiveData<Resource<List<Contributor>>> contributors;

    @Inject
    public RepoViewModel(final RepoRepository repository) {
        this.repoId = new MutableLiveData<>();
        repo = Transformations.switchMap(repoId, input ->{
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            }
            return repository.loadRepo(input.owner, input.name);
        });
    }

    @VisibleForTesting
    static class RepoId {
        public final String owner;
        public final String name;

        RepoId(String owner, String name) {
            this.owner = owner == null ? null : owner.trim();
            this.name = name == null ? null : name.trim();
        }

        boolean isEmpty() {
            return owner == null || name == null || owner.length() == 0 || name.length() == 0;
        }
    }
}
