package com.github.luecy1.androidstudyapp.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.luecy1.androidstudyapp.db.GithubTypeConverters;

import java.util.List;

/**
 * Created by you on 2018/01/31.
 */
@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class RepoSearchResult {
    @NonNull
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCOunt;

    @Nullable
    public final Integer next;

    public RepoSearchResult(@NonNull String query,
                            List<Integer> reoiIds,
                            int totalCOunt,
                            @Nullable Integer next) {
        this.query = query;
        this.repoIds = reoiIds;
        this.totalCOunt = totalCOunt;
        this.next = next;
    }
}
