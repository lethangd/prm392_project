package com.example.englishkids.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grammar_type")
public class GrammarType {

    @PrimaryKey(autoGenerate = true)
    private int grammarTypeId;

    private String pattern;
    private String description;


    // Constructors, getters, and setters
    public GrammarType(int grammarTypeId, String pattern, String description) {
        this.grammarTypeId = grammarTypeId;
        this.pattern = pattern;
        this.description = description;
    }

    public int getGrammarTypeId() {
        return grammarTypeId;
    }

    public void setGrammarTypeId(int grammarTypeId) {
        this.grammarTypeId = grammarTypeId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
