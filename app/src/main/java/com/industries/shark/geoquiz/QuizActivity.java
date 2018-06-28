package com.industries.shark.geoquiz;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "Quiz Activity";
    private static final String KEY_INDEX = "index";
    private static final String SCORE_INDEX = "mScore";
    private static final String TOTAL_ANSWERS_INDEX = "total";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String IS_CHEATER_KEY = "isCheater";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private boolean mIsCheater;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mScore = 0;
    private int mTotalAnswers = 0;
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null){

            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mScore = savedInstanceState.getInt(SCORE_INDEX, 0);
            mTotalAnswers = savedInstanceState.getInt(SCORE_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER_KEY, false);

            int i = 0;

            for (Question q:mQuestionBank) {
               q.setAnswered(savedInstanceState.getBoolean("q" + i, false));
               q.setCheatedQuestion(savedInstanceState.getBoolean("c" + i, false));
               i++;
            }

        }

        mQuestionTextView =(TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               checkAnswer(true);


            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);




            }

        });

            mNextButton = (ImageButton) findViewById(R.id.next_button);
            mNextButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    mIsCheater = false;
                        updateQuestion();


                }

            });

            mPrevButton = (ImageButton) findViewById(R.id.prev_button);
            mPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCurrentIndex>0) {
                        mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                        mIsCheater = false;
                        updateQuestion();
                    }
                }
            });

            mCheatButton = (Button) findViewById(R.id.cheat_button);
            mCheatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                   startActivityForResult(intent, REQUEST_CODE_CHEAT);
                }
            });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mNextButton.callOnClick();
            }
        });
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mQuestionBank[mCurrentIndex].setCheatedQuestion(true);
        }
    }

    private void answerButtonsController(){
        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false);
        }
        else {
            mTrueButton.setClickable(true);
            mFalseButton.setClickable(true);

        }
    }
    private void showResults(){
        double percentScore = 0;
        if (mScore >0) {
            percentScore = Math.round(((double) mScore / (double) mTotalAnswers) * 100);
        }
                     Toast.makeText(this,
                "Your Score is " + mScore + " of " + mTotalAnswers + " it`s " + percentScore + "%",
                     Toast.LENGTH_LONG).show();
        }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(SCORE_INDEX, mScore);
        outState.putInt(TOTAL_ANSWERS_INDEX, mTotalAnswers);
        outState.putBoolean(IS_CHEATER_KEY, mIsCheater);
        int i = 0;
        for (Question q:mQuestionBank) {
            outState.putBoolean("q" + i,q.isAnswered());
            outState.putBoolean("c" + i, q.isCheatedQuestion());
            i++;
        }

    }

    private void updateQuestion() {

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        answerButtonsController();

    }

    private void checkAnswer(boolean userPressedTrue){

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if(mQuestionBank[mCurrentIndex].isCheatedQuestion()){
            messageResId = R.string.judgement_toast;
        }
        else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else {
                messageResId = R.string.incorrect_toast;
            }

        }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        mQuestionBank[mCurrentIndex].setAnswered(true);

        mTotalAnswers++;

        answerButtonsController();

        if (mTotalAnswers == mQuestionBank.length){
            showResults();
        }
    }

}
