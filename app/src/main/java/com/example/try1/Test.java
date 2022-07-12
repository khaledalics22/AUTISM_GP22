package com.example.try1;
public class Test {
    private String test[]={
            "Angry"     ,"Sad"    ,"Cry"       ,"Board"      ,"Exhausted","Thirsty","Proud","Sorry",
            "Happy"     ,"Worried","Cold"       ,"Funny"      ,"Disgusted","Hungary","Pain" ,"Sleepy",
            "Hungary"   ,"Hot"    ,"Exhausted"  ,"Embarrassed","Excited" ,"Guilty" ,"Cold" ,"Sick",
            "Scared"    ,"Proud"   ,"Thirsty"   ,"Confused"   ,"Furious","Love"   ,"Hot"  ,"Worried"
    };
    private int choice[][]={
            {R.drawable.angry_,R.drawable.happy,R.drawable.hungry, R.drawable.scared},
            {R.drawable.proud,R.drawable.sad,R.drawable.worried, R.drawable.hot},
            {R.drawable.exhuasted,R.drawable.cold,R.drawable.cry_, R.drawable.thirsty},
            {R.drawable.board,R.drawable.funny,R.drawable.embarrassed, R.drawable.confused},
            {R.drawable.excited,R.drawable.exhuasted,R.drawable.disgused, R.drawable.furious},
            {R.drawable.guilty,R.drawable.hungry,R.drawable.thirsty, R.drawable.love_},
            {R.drawable.hot,R.drawable.cold,R.drawable.pain, R.drawable.proud},
            {R.drawable.sick,R.drawable.sorry,R.drawable.sleepy, R.drawable.worried},
//////////////////////////////////////
            {R.drawable.angry_,R.drawable.happy,R.drawable.hungry, R.drawable.scared},
            {R.drawable.proud,R.drawable.sad,R.drawable.worried, R.drawable.hot},
            {R.drawable.exhuasted,R.drawable.cold,R.drawable.cry_, R.drawable.thirsty},
            {R.drawable.board,R.drawable.funny,R.drawable.embarrassed, R.drawable.confused},
            {R.drawable.excited,R.drawable.exhuasted,R.drawable.disgused, R.drawable.furious},
            {R.drawable.guilty,R.drawable.hungry,R.drawable.thirsty, R.drawable.love_},
            {R.drawable.hot,R.drawable.cold,R.drawable.pain, R.drawable.proud},
            {R.drawable.sick,R.drawable.sorry,R.drawable.sleepy, R.drawable.worried},
////////////////////////////////////////
            {R.drawable.angry_,R.drawable.happy,R.drawable.hungry, R.drawable.scared},
            {R.drawable.proud,R.drawable.sad,R.drawable.worried, R.drawable.hot},
            {R.drawable.exhuasted,R.drawable.cold,R.drawable.cry_, R.drawable.thirsty},
            {R.drawable.board,R.drawable.funny,R.drawable.embarrassed, R.drawable.confused},
            {R.drawable.excited,R.drawable.exhuasted,R.drawable.disgused, R.drawable.furious},
            {R.drawable.guilty,R.drawable.hungry,R.drawable.thirsty, R.drawable.love_},
            {R.drawable.hot,R.drawable.cold,R.drawable.pain, R.drawable.proud},
            {R.drawable.sick,R.drawable.sorry,R.drawable.sleepy, R.drawable.worried},
//////////////////////////////////////
            {R.drawable.angry_,R.drawable.happy,R.drawable.hungry, R.drawable.scared},
            {R.drawable.proud,R.drawable.sad,R.drawable.worried, R.drawable.hot},
            {R.drawable.exhuasted,R.drawable.cold,R.drawable.cry_, R.drawable.thirsty},
            {R.drawable.board,R.drawable.funny,R.drawable.embarrassed, R.drawable.confused},
            {R.drawable.excited,R.drawable.exhuasted,R.drawable.disgused, R.drawable.furious},
            {R.drawable.guilty,R.drawable.hungry,R.drawable.thirsty, R.drawable.love_},
            {R.drawable.hot,R.drawable.cold,R.drawable.pain, R.drawable.proud},
            {R.drawable.sick,R.drawable.sorry,R.drawable.sleepy, R.drawable.worried},
////////////////////////////////////////

    };
    private int answer[]={0,1,2,0,1,2,3,1,
                          1,2,1,1,2,1,2,2,
                          2,3,0,2,0,0,1,0,
                          3,0,3,3,3,3,0,3};
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
