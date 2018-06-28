package com.industries.shark.geoquiz;

/**
 * Created by Кирилл on 18.06.2018.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsAnswered = false;
    private boolean mCheatedQuestion;
    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public boolean isAnswered() {
        return mIsAnswered;
    }

    public void setAnswered(boolean answered) {
        mIsAnswered = answered;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isCheatedQuestion() {
        return mCheatedQuestion;
    }

    public void setCheatedQuestion(boolean cheatedQuestion) {
        mCheatedQuestion = cheatedQuestion;
    }
}
