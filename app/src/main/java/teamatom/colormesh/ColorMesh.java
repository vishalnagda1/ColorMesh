package teamatom.colormesh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ColorMesh extends AppCompatActivity {
    long inc;
    int total;
    CountDownTimer ct;
    DatabaseHelper myDb;
    int[] color_code = {Color.BLUE,Color.GREEN,Color.GRAY,Color.RED,Color.YELLOW};
    String[] color_name;
    int[] random_code = {(int) (Math.random()*color_code.length),(int) (Math.random()*color_code.length),(int) (Math.random()*color_code.length)};
    public static String score = "score",tout="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_mesh);
        color_name = getResources().getStringArray(R.array.color_name);
        inc=1;
        total=60000;
        myDb = new DatabaseHelper(this);

        final TextView current_score = (TextView) findViewById(R.id.score);
        final TextView high_score = (TextView) findViewById(R.id.hscore);
        final ProgressBar timestamp = (ProgressBar) findViewById(R.id.timestamp);
        ImageButton right = (ImageButton) findViewById(R.id.right);
        ImageButton wrong = (ImageButton) findViewById(R.id.wrong);
        final Intent intent = new Intent(this,Result.class);
        timestamp.setProgress(0);
        ct=new CountDownTimer(total, inc) {

            public void onTick(long millisUntilFinished) {
                timestamp.setMax(total);
                timestamp.setProgress(total-(int)millisUntilFinished);
            }

            public void onFinish() {
                intent.putExtra(score, current_score.getText());
                intent.putExtra(tout, "1");
                startActivity(intent);
                finish();
            }
        };
        ct.start();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            high_score.setText("0");
            myDb.insertData("0","0");
        }
        else {
            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext())
                buffer.append(res.getString(0));
            high_score.setText(buffer.toString());

        }

        draw();

        right.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (random_code[0] == random_code[2]) {
                    current_score.setText(Integer.toString(Integer.parseInt(current_score.getText().toString()) + 1));
                    total = total - total / 10;
                    if (total < 2000 && Integer.parseInt(current_score.getText().toString()) > 60) {
                        if (total < 1000) total = 1000;
                    } else {
                        if (total < 2000) total = 2000;
                    }
                    ct.cancel();
                    ct = new CountDownTimer(total, inc) {

                        public void onTick(long millisUntilFinished) {
                            timestamp.setMax(total);
                            timestamp.setProgress(total - (int) millisUntilFinished);
                        }

                        public void onFinish() {
                            intent.putExtra(score, current_score.getText());
                            intent.putExtra(tout, "1");
                            startActivity(intent);
                            finish();
                        }
                    };
                    ct.start();
                } else {
                    ct.cancel();
                    intent.putExtra(score, current_score.getText());
                    intent.putExtra(tout, "0");
                    startActivity(intent);
                    finish();
                }
                random_value();

                draw();
            }
        });

        wrong.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (random_code[0] != random_code[2]) {
                    current_score.setText(Integer.toString(Integer.parseInt(current_score.getText().toString()) + 1));
                    total = total - total / 10;

                    if (total < 2000 && Integer.parseInt(current_score.getText().toString()) > 60) {
                        if (total < 1000) total = 1000;
                    } else {
                        if (total < 2000) total = 2000;
                    }
                    ct.cancel();
                    ct = new CountDownTimer(total, inc) {

                        public void onTick(long millisUntilFinished) {
                            timestamp.setMax(total);
                            timestamp.setProgress(total - (int) millisUntilFinished);
                        }

                        public void onFinish() {
                            intent.putExtra(score, current_score.getText());
                            intent.putExtra(tout, "1");
                            startActivity(intent);
                            finish();
                        }
                    };
                    ct.start();
                } else {
                    ct.cancel();
                    intent.putExtra(score, current_score.getText());
                    intent.putExtra(tout, "0");
                    startActivity(intent);
                    finish();
                }

                random_value();

                draw();
            }
        });

    }

    void draw () {

        while (true) {
            if (random_code[0] != random_code[1]) break;
            random_code[0] = (int) (Math.random()*color_code.length);
        }

        Paint paint = new Paint();
        paint.setColor(color_code[random_code[0]]);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        ImageView c = (ImageView) findViewById(R.id.circle);

        Bitmap bitmap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
            bitmap = Bitmap.createBitmap((int) (c.getMaxWidth() * 0.000233) / 1000, (int) (c.getMaxWidth() * 0.000233) / 1000, Bitmap.Config.ARGB_8888);
        else bitmap = Bitmap.createBitmap(660, 660, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(bitmap.getHeight() / 2, bitmap.getWidth() / 2, 250, paint);

        ((ImageView) findViewById(R.id.circle)).setImageBitmap(bitmap);
        ((TextView) findViewById(R.id.cname)).setText(color_name[random_code[2]]);
        ((TextView) findViewById(R.id.cname)).setTextColor(color_code[random_code[1]]);

    }

    void random_value () {

        random_code[0] = (int) (Math.random()*color_code.length);
        random_code[1] = (int) (Math.random()*color_code.length);
        random_code[2] = (int) (Math.random()*color_code.length);

    }

    @Override
    public void onBackPressed() {
        ct.cancel();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }

}