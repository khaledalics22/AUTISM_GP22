package com.example.Objects;

public class Score {
    int correct_sad, error_sad;
    int correct_happy, error_happy;
    int correct_natural, error_natural;
    int correct_surprised, error_surprised;

    public Score() {
        correct_happy = 0;
        correct_sad = 0;
        correct_natural = 0;
        correct_surprised = 0;
        error_happy = 0;
        error_sad = 0;
        error_natural = 0;
        error_surprised = 0;

    }

    public float getTotalScore() {
        float correct = correct_happy + correct_sad + correct_natural + correct_surprised;
        return correct / (correct + error_sad + error_happy + error_natural + error_surprised);
    }

    public int getCorrect_sad() {
        return correct_sad;
    }

    public int getError_sad() {
        return error_sad;
    }

    public int getCorrect_happy() {
        return correct_happy;
    }

    public int getError_happy() {
        return error_happy;
    }

    public int getCorrect_natural() {
        return correct_natural;
    }

    public int getError_natural() {
        return error_natural;
    }

    public int getCorrect_surprised() {
        return correct_surprised;
    }

    public int getError_surprised() {
        return error_surprised;
    }

    public void increaseCorrectSad() {
        correct_sad++;
    }

    public void increaseCorrectHappy() {
        correct_happy++;
    }

    public void increaseCorrectNatural() {
        correct_natural++;
    }

    public void increaseCorrectSurprised() {
        correct_surprised++;
    }

    public void increaseErrorSad() {
        error_sad++;
    }

    public void increaseErrorHappy() {
        error_happy++;
    }

    public void increaseErrorNatural() {
        error_natural++;
    }

    public void increaseErrorSurprised() {
        error_surprised++;
    }


    public void clear() {
        correct_happy = 0;
        correct_natural = 0;
        correct_sad = 0;
        correct_surprised = 0;
        error_happy = 0;
        error_natural = 0;
        error_sad = 0;
        error_surprised = 0;
    }

    public boolean notEmpty() {
        return correct_happy != 0
                || correct_natural != 0
                || correct_sad != 0
                || correct_surprised != 0
                || error_happy != 0
                || error_natural != 0
                || error_sad != 0
                || error_surprised != 0;
    }
}
