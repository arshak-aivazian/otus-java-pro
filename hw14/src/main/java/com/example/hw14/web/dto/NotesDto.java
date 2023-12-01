package com.example.hw14.web.dto;

import java.util.List;

public class NotesDto {
    private int note100;
    private int note500;
    private int note1000;
    private int note5000;

    public NotesDto() {
    }

    public NotesDto(int note100, int note500, int note1000, int note5000) {
        this.note100 = note100;
        this.note500 = note500;
        this.note1000 = note1000;
        this.note5000 = note5000;
    }

    public int getNote100() {
        return note100;
    }

    public int getNote500() {
        return note500;
    }

    public int getNote1000() {
        return note1000;
    }

    public int getNote5000() {
        return note5000;
    }

    public void setNote100(int note100) {
        this.note100 = note100;
    }

    public void setNote500(int note500) {
        this.note500 = note500;
    }

    public void setNote1000(int note1000) {
        this.note1000 = note1000;
    }

    public void setNote5000(int note5000) {
        this.note5000 = note5000;
    }

    public List<Integer> toList() {
        return List.of(note5000, note1000, note500, note100);
    }
}
