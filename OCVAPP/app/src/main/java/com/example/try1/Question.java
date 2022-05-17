package com.example.try1;

import com.example.ocvapp.R;

public class Question {
    private String question[] = {
            "Angry", "Sad", "Cry", "board", "exhausted", "thirsty", "proud", "sorry"
    };
    private int choice[][] = {
            {R.drawable.angry_, R.drawable.happy, R.drawable.hungry, R.drawable.scared},
            {R.drawable.proud, R.drawable.sad, R.drawable.worried, R.drawable.hot},
            {R.drawable.exhuasted, R.drawable.cold, R.drawable.cry_, R.drawable.thirsty},
            {R.drawable.board, R.drawable.funny, R.drawable.embarrassed, R.drawable.confused},
            {R.drawable.excited, R.drawable.exhuasted, R.drawable.disgused, R.drawable.furious},
            {R.drawable.guilty, R.drawable.hungry, R.drawable.thirsty, R.drawable.love},
            {R.drawable.hot, R.drawable.cold, R.drawable.pain, R.drawable.proud},
            {R.drawable.sick, R.drawable.sorry, R.drawable.sleepy, R.drawable.worried},

    };
    private int answer[] = {
            0, 1, 2, 0, 1, 2, 3, 1
    };

    public String[] getQuestion() {
        return question;
    }

    public void setQuestion(String[] question) {
        this.question = question;
    }

    public int[][] getChoice() {
        return choice;
    }

    public void setChoice(int[][] choice) {
        this.choice = choice;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }
}
