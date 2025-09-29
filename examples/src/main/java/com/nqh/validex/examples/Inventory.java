package com.nqh.validex.examples;

import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.annotations.NotEmpty;
import com.nqh.validex.annotations.Size;

import java.util.List;
import java.util.Map;

public class Inventory {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 3)
    private List<String> tags;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 2)
    private Map<String, String> attributes;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 4)
    private int[] scores;

    public Inventory(List<String> tags, Map<String, String> attributes, int[] scores) {
        this.tags = tags;
        this.attributes = attributes;
        this.scores = scores;
    }

    public List<String> getTags() {
        return tags;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public int[] getScores() {
        return scores;
    }
}
