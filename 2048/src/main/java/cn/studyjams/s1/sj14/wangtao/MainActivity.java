package cn.studyjams.s1.sj14.wangtao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private TextView scoreTextView, bestTextView;
    private TextView[][] textViewGroup = new TextView[4][4];
    protected int[][] numbersArrayA = new int[4][4];
    protected int[][] numbersArrayB = new int[4][4];
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private int randomNumber = 2, zero, place, scoreA = 0, bestScore = 0, scoreB = 0;
    private final static int UP = 1;
    private final static int DOWN = 2;
    private final static int LEFT = 3;
    private final static int RIGHT = 4;
    private SQLite sqLite;
    private SQLiteDatabase db;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.end, 1);

        sqLite = new SQLite(this);

        gestureDetector = new GestureDetector(this, new iTouch());

        initView();

        fillInTwo();

        fillInTwo();

        setValusOfTextViewGroup();

        displayBestScore();
    }

    public void onClick(View view){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                numbersArrayA[i][j] = 0;
                numbersArrayB[i][j] = 0;
            }
        }
        fillInTwo();
        fillInTwo();
        setValusOfTextViewGroup();
        scoreTextView.setText(0+"");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class iTouch implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent e_1, MotionEvent e_2, float v, float v1) {
            float x_1 = e_1.getX();
            float x_2 = e_2.getX();
            float y_1 = e_1.getY();
            float y_2 = e_2.getY();

            float k = Math.abs((y_2 - y_1) / (x_2 - x_1));

            if (k < 1) {
                if (x_1 - x_2 > 50) {
                    orientation(LEFT);
                } else if (x_2 - x_1 > 50) {
                    orientation(RIGHT);
                }
            } else {
                if (y_1 - y_2 > 50) {
                    orientation(UP);
                } else if (y_2 - y_1 > 50) {
                    orientation(DOWN);
                }
            }
            return false;
        }
    }

    private void initView() {

        textViewGroup[0][0] = (TextView) findViewById(R.id.text_view_1);
        textViewGroup[0][1] = (TextView) findViewById(R.id.text_view_2);
        textViewGroup[0][2] = (TextView) findViewById(R.id.text_view_3);
        textViewGroup[0][3] = (TextView) findViewById(R.id.text_view_4);

        textViewGroup[1][0] = (TextView) findViewById(R.id.text_view_5);
        textViewGroup[1][1] = (TextView) findViewById(R.id.text_view_6);
        textViewGroup[1][2] = (TextView) findViewById(R.id.text_view_7);
        textViewGroup[1][3] = (TextView) findViewById(R.id.text_view_8);

        textViewGroup[2][0] = (TextView) findViewById(R.id.text_view_9);
        textViewGroup[2][1] = (TextView) findViewById(R.id.text_view_10);
        textViewGroup[2][2] = (TextView) findViewById(R.id.text_view_11);
        textViewGroup[2][3] = (TextView) findViewById(R.id.text_view_12);

        textViewGroup[3][0] = (TextView) findViewById(R.id.text_view_13);
        textViewGroup[3][1] = (TextView) findViewById(R.id.text_view_14);
        textViewGroup[3][2] = (TextView) findViewById(R.id.text_view_15);
        textViewGroup[3][3] = (TextView) findViewById(R.id.text_view_16);

        scoreTextView = (TextView) findViewById(R.id.score_text_view);
        bestTextView = (TextView) findViewById(R.id.best_text_view);
    }

    public void orientation(int o) {
        boolean exit = false;
        boolean end = false;
        int j_1 = 1;

        switch (o) {
            case DOWN:
                m_Up_Down(3, 0, -1);
                break;
            case UP:
                m_Up_Down(0, 0, 1);
                break;
            case RIGHT:
                m_Left_Right(0, 3, -1);
                break;
            case LEFT:
                m_Left_Right(0, 0, 1);
                break;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (numbersArrayB[i][j] != numbersArrayA[i][j]) {
                    exit = true;
                    break;
                }
            }
            if (exit) {
                break;
            }
        }

        if (exit) {
            fillInTwo();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    numbersArrayB[i][j] = numbersArrayA[i][j];
                }
            }
            setValusOfTextViewGroup();
            scoreB = scoreA;
        } else {

            if (zero == 1) {
                for (int i = 0; i < 4; i++) {
                    if (i % 2 == 1) {
                        j_1 = 1;
                    } else {
                        j_1 = 0;
                    }
                    int j = 0;
                    for (j += j_1; j < 4; j += 2) {

                        end = isEnd(i, j);
                        if (end) {
                            break;
                        }
                    }
                    if (end) {
                        break;
                    }
                }
                if (end == false) {
                    alert = null;
                    builder = new AlertDialog.Builder(this);

                    alert = builder.setTitle("Hello")
                            .setMessage("You failed!")
                            .setNegativeButton("quit", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveScore();
                                    finish();
                                }
                            })
                            .setPositiveButton("replay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < 4; i++) {
                                        for (int j = 0; j < 4; j++) {
                                            numbersArrayA[i][j] = 0;
                                            numbersArrayB[i][j] = 0;
                                        }
                                    }
                                    fillInTwo();
                                    fillInTwo();
                                    setValusOfTextViewGroup();
                                    displayBestScore();
                                    scoreTextView.setText(0+"");
                                    bestTextView.setText(bestScore + "");
                                }
                            }).create();

                    alert.show();

                    soundPool.play(1, 4, 4, 0, 0, 1);

                    saveScore();

                    scoreA = 0;
                }
            }
        }

    }

    public void setValusOfTextViewGroup() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                setColorAndValuseOfTextViewGroup(i, j, numbersArrayA[i][j]);
            }
        }
    }

    private void setColorAndValuseOfTextViewGroup(int i, int j, int num) {
        if (num == 0) {
            textViewGroup[i][j].setText("");
        } else {
            textViewGroup[i][j].setText(num + "");
        }
        switch (num) {
            case 0:
                textViewGroup[i][j].setBackgroundColor(0x33ffffff);
                break;
            case 2:
                textViewGroup[i][j].setBackgroundColor(0xffeee4da);
                break;
            case 4:
                textViewGroup[i][j].setBackgroundColor(0xffede0c8);
                break;
            case 8:
                textViewGroup[i][j].setBackgroundColor(0xfff2b179);
                break;
            case 16:
                textViewGroup[i][j].setBackgroundColor(0xfff59563);
                break;
            case 32:
                textViewGroup[i][j].setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                textViewGroup[i][j].setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                textViewGroup[i][j].setBackgroundColor(0xffedcf72);
                break;
            case 256:
                textViewGroup[i][j].setBackgroundColor(0xffedcc61);
                break;
            case 512:
                textViewGroup[i][j].setBackgroundColor(0xffedc850);
                break;
            case 1024:
                textViewGroup[i][j].setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                textViewGroup[i][j].setBackgroundColor(0xffedc22e);
                break;
            default:
                textViewGroup[i][j].setBackgroundColor(0xff3c3a32);
                break;
        }
    }

    public void getScore(int v) {
        scoreA += v;
        scoreTextView.setText(scoreA + "");
    }

    public void getRandom() {

        randomNumber = (Math.random() < 0.9 ? 2 : 4);
    }

    public void getZero() {
        int mZero = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (numbersArrayA[i][j] == 0) {
                    mZero++;
                }
            }
        }
        zero = mZero;

    }

    public void getPlace() {
        Random random = new Random();
        place = random.nextInt(zero);


    }

    public void fillInTwo() {
        int mZero = 0;
        boolean exit = false;
        getZero();
        getPlace();
        getRandom();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (numbersArrayA[i][j] == 0) {
                    if (mZero == place) {
                        numbersArrayA[i][j] = randomNumber;
                        exit = true;
                        break;
                    } else
                        mZero++;
                }
            }
            if (exit)
                break;

        }
    }


    public void m_Up_Down(int h, int l, int h_) {

        int some;
        if (h_ < 0)
            some = 3;
        else {
            some = 0;
        }
        for (int i = 0; i < 4; i++) {
            zero_Up_Down(some, i, h_);
            for (int j = 1; j <= 4 - 1; j++) {
                int h_1 = h + j * h_;
                int h_2 = h + (j - 1) * h_;
                int l_ = l + i;

                if (numbersArrayA[h_1][l_] == numbersArrayA[h_2][l_]) {
                    numbersArrayA[h_2][l_] = numbersArrayA[h_2][l_] * 2;
                    numbersArrayA[h_1][l_] = 0;
                    getScore(numbersArrayA[h_2][l_]);
                    j++;
                }
            }
            zero_Up_Down(some, i, h_);
        }
    }

    public void m_Left_Right(int h, int l, int l_) {
        int i, j;

        int some;
        if (l_ < 0)
            some = 3;
        else {
            some = 0;
        }

        for (j = 0; j < 4; j++) {

            zero_Left_Right(j, some, l_);
            for (i = 1; i <= 4 - 1; i++) {
                int l_1 = l + i * l_;
                int l_2 = l + (i - 1) * l_;
                int h_ = h + j;

                if (numbersArrayA[h_][l_1] == numbersArrayA[h_][l_2]) {
                    numbersArrayA[h_][l_2] = numbersArrayA[h_][l_2] * 2;
                    numbersArrayA[h_][l_1] = 0;
                    getScore(numbersArrayA[h_][l_2]);
                    i++;

                }
            }
            zero_Left_Right(j, some, l_);
        }

    }

    public void zero_Up_Down(int h, int l, int o) {
        for (int i = 0; i < 3; i++) {
            if (numbersArrayA[h + i * o][l] == 0) {
                for (int j = i; j < 4; j++) {
                    if (numbersArrayA[h + j * o][l] != 0) {
                        numbersArrayA[h + i * o][l] = numbersArrayA[h + j * o][l];
                        numbersArrayA[h + j * o][l] = 0;
                        break;
                    }

                }
            }
        }

    }

    public void zero_Left_Right(int h, int l, int o) {
        for (int i = 0; i < 3; i++) {
            if (numbersArrayA[h][l + i * o] == 0) {
                for (int j = i; j < 4; j++) {
                    if (numbersArrayA[h][l + j * o] != 0) {
                        numbersArrayA[h][l + i * o] = numbersArrayA[h][l + j * o];
                        numbersArrayA[h][l + j * o] = 0;
                        break;
                    }

                }
            }
        }

    }

    public void saveScore() {
        String msg = scoreB + "";
        db = sqLite.getWritableDatabase();
        db.execSQL("INSERT INTO grade(Score)VALUES(?)", new String[]{msg});
    }

    public void displayBestScore() {
        db = sqLite.getReadableDatabase();
        Cursor cursor = db.query("grade", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String best = cursor.getString(cursor.getColumnIndex("Score"));
            int score = Integer.parseInt(best);
            if (bestScore <= score) {
                bestScore = score;
            }
        }
        bestTextView.setText(bestScore + "");
        cursor.close();
    }

    public boolean isEnd(int h, int l) {
        if (h - 1 >= 0 && h - 1 <= 3) {
            if (numbersArrayA[h - 1][l] == numbersArrayA[h][l])
                return true;
        }
        if (h + 1 >= 0 && h + 1 <= 3) {
            if (numbersArrayA[h + 1][l] == numbersArrayA[h][l])
                return true;
        }
        if (l - 1 >= 0 && l - 1 <= 3) {
            if (numbersArrayA[h][l - 1] == numbersArrayA[h][l])
                return true;
        }
        if (l + 1 >= 0 && l + 1 <= 3) {
            if (numbersArrayA[h][l + 1] == numbersArrayA[h][l])
                return true;
        }

        return false;
    }
}
