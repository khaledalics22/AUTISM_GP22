package com.example.GUI;

import com.example.ocvapp.R;

public class Test {
    private String test[]={
            "Happy","Happy","Happy","Happy",
            "Sad","Sad","Sad","Sad",
            "Normal","Normal","Normal","Normal",
            "Surprised","Surprised","Surprised","Surprised"
//            0,3,2,1   1,0,3,2     ,3,2,1,0    ,2,1,0,3
//            "Happy" ,"Sad","Normal","Surprised" ,
//            "Surprised"     ,"Happy","Normal","Sad" ,
//            "Happy"     ,"Normal","Sad","Surprised" ,
//            "Normal"     ,"Sad","Happy","Surprised"
    };
    private int choice[][]={
            {R.drawable.h1,R.drawable.s1,R.drawable.sr1, R.drawable.n1},
            {R.drawable.s2,R.drawable.sr2,R.drawable.n2, R.drawable.h2},
            {R.drawable.sr3,R.drawable.n3,R.drawable.h3, R.drawable.s3},
            {R.drawable.n4,R.drawable.h4,R.drawable.s4, R.drawable.sr3},

            {R.drawable.h1,R.drawable.s1,R.drawable.sr1, R.drawable.n1},
            {R.drawable.s2,R.drawable.sr2,R.drawable.n2, R.drawable.h2},
            {R.drawable.sr3,R.drawable.n3,R.drawable.h3, R.drawable.s3},
            {R.drawable.n4,R.drawable.h4,R.drawable.s4, R.drawable.sr3},

            {R.drawable.h1,R.drawable.s1,R.drawable.sr1, R.drawable.n1},
            {R.drawable.s2,R.drawable.sr2,R.drawable.n2, R.drawable.h2},
            {R.drawable.sr3,R.drawable.n3,R.drawable.h3, R.drawable.s3},
            {R.drawable.n4,R.drawable.h4,R.drawable.s4, R.drawable.sr3},
//
            {R.drawable.h1,R.drawable.s1,R.drawable.sr1, R.drawable.n1},
            {R.drawable.s2,R.drawable.sr2,R.drawable.n2, R.drawable.h2},
            {R.drawable.sr3,R.drawable.n3,R.drawable.h3, R.drawable.s3},
            {R.drawable.n4,R.drawable.h4,R.drawable.s4, R.drawable.sr3},
    };
//    private int answer[]={0,0,1,3, 2,3,1,2 ,0,2,3,3,0,0,2,3};
    private int answer[]={0,3,2,1,1,0,3,2,3,2,1,0,2,1,0,3};

    public String[] getTest() {
        return test;
    }
    public void setQuestion(String[] test) {
        this.test = test;
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
