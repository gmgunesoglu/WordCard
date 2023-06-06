package com.mobilproje.wordcard.model;

import java.sql.Timestamp;
import java.util.List;

public class Card {

    private Long id;
    private Long userId;
    private Long trWordId;
    private String trWord;
    private List<String> trUsage;
    private Long engWordId;
    private String engWord;
    private List<String> engUsage;
    private int correctAnswer;
    private int wrongAnswer;
    private Timestamp lastUsage;
    private Timestamp added;

    public Card() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTrWordId() {
        return trWordId;
    }

    public void setTrWordId(Long trWordId) {
        this.trWordId = trWordId;
    }

    public String getTrWord() {
        return trWord;
    }

    public void setTrWord(String trWord) {
        this.trWord = trWord;
    }

    public List<String> getTrUsage() {
        return trUsage;
    }

    public void setTrUsage(List<String> trUsage) {
        this.trUsage = trUsage;
    }

    public Long getEngWordId() {
        return engWordId;
    }

    public void setEngWordId(Long engWordId) {
        this.engWordId = engWordId;
    }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public List<String> getEngUsage() {
        return engUsage;
    }

    public void setEngUsage(List<String> engUsage) {
        this.engUsage = engUsage;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public Timestamp getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Timestamp lastUsage) {
        this.lastUsage = lastUsage;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", userId=" + userId +
                ", trWordId=" + trWordId +
                ", trWord='" + trWord + '\'' +
                ", trUsage=" + trUsage +
                ", engWordId=" + engWordId +
                ", engWord='" + engWord + '\'' +
                ", engUsage=" + engUsage +
                ", correctAnswer=" + correctAnswer +
                ", wrongAnswer=" + wrongAnswer +
                ", lastUsage=" + lastUsage +
                ", added=" + added +
                '}';
    }
}
