package club.cppcss.catordog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String animalChosen = "";
    int score = 0;
    int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        highScore = pref.getInt("highScore", 0);
        TextView highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setText("High Score: " + highScore);
    }

    private String getAnimal() {
        TextView animalType = (TextView) findViewById(R.id.animalType);
        Random rand = new Random();
        double randomNumber = rand.nextFloat();

        if(randomNumber > 0.5) {
            animalType.setText("Dog");
            return "Dog";
        } else {
            animalType.setText("Cat");
            return "Cat";
        }
    }

    public void startGame(View view) {
        score = 0;
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText("Score: " + score);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button catButton = (Button) findViewById(R.id.catButton);
        Button dogButton = (Button) findViewById(R.id.dogButton);
        startButton.setVisibility(View.GONE);
        catButton.setVisibility(View.VISIBLE);
        dogButton.setVisibility(View.VISIBLE);

        countdown();

    }



    private void countdown() {
        animalChosen = "";
        new CountDownTimer(3500, 1000) {
            String animal = getAnimal();
            boolean correctAnswer = false;
            TextView animalType = (TextView) findViewById(R.id.animalType);
            TextView scoreText = (TextView) findViewById(R.id.scoreText);

            public void onTick(long millisUntilFinished) {
               if(animalChosen.equals(animal)) {
                   score++;
                   scoreText.setText("Score: " + score);
                   correctAnswer = true;
                   countdown();
                   this.cancel();
               } else if(animalChosen != ""){
                   correctAnswer = false;
                   animalType.setText("Game Over!");
                   Button startButton = (Button) findViewById(R.id.startButton);
                   Button catButton = (Button) findViewById(R.id.catButton);
                   Button dogButton = (Button) findViewById(R.id.dogButton);
                   startButton.setVisibility(View.VISIBLE);
                   catButton.setVisibility(View.GONE);
                   dogButton.setVisibility(View.GONE);
                   if(score > highScore) {
                       highScore = score;
                       editor.putInt("highScore", score); // Storing integer
                       editor.commit();
                       TextView highScoreText = (TextView) findViewById(R.id.highScore);
                       highScoreText.setText("High Score: " + highScore);
                   }
                   this.cancel();
               }
            }

            public void onFinish() {
                if(!correctAnswer) {
                    animalType.setText("Game Over!");
                    Button startButton = (Button) findViewById(R.id.startButton);
                    Button catButton = (Button) findViewById(R.id.catButton);
                    Button dogButton = (Button) findViewById(R.id.dogButton);
                    startButton.setVisibility(View.VISIBLE);
                    catButton.setVisibility(View.GONE);
                    dogButton.setVisibility(View.GONE);
                    if(score > highScore) {
                        highScore = score;
                        editor.putInt("highScore", score); // Storing integer
                        editor.commit();
                        TextView highScoreText = (TextView) findViewById(R.id.highScore);
                        highScoreText.setText("High Score: " + highScore);
                    }
                }
            }
        }.start();

    }

    public void catButtonClicked(View v) {
        animalChosen = "Cat";
    }

    public void dogButtonClicked(View v) {
        animalChosen = "Dog";
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "animal api");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
