package club.cppcss.catordog;

import android.content.res.AssetManager;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Random rand = new Random();
    String animalChosen = "";
    int score = 0;
    int highScore = 0;

    Drawable[] catDrawables;
    Drawable[] dogDrawables;

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

        catDrawables = getDrawablesFromAssetFolder("cats");
        dogDrawables = getDrawablesFromAssetFolder("dogs");
    }

    private Drawable[] getDrawablesFromAssetFolder(String folderName) {
        try {
            AssetManager assetManager = getAssets();
            String[] images = assetManager.list(folderName);
            Drawable[] drawables = new Drawable[images.length];

            InputStream inputStream;

            for (int i = 0; i < images.length; i++) {
                inputStream = getAssets().open(folderName + "/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;
            }

            return drawables;
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
            return null;
        }
    }

    private String getAnimal() {
        ImageView animalImage = (ImageView) findViewById(R.id.animalImage);
        double randomNumber = rand.nextFloat();

        if(randomNumber > 0.5) {
            animalImage.setImageDrawable(getRandomAnimalImage(true));
            return "Dog";
        } else {
            animalImage.setImageDrawable(getRandomAnimalImage(false));
            return "Cat";
        }
    }

    public void startGame(View view) {
        score = 0;
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        TextView gameOverText = (TextView) findViewById(R.id.gameOverText);
        scoreText.setText("Score: " + score);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button catButton = (Button) findViewById(R.id.catButton);
        Button dogButton = (Button) findViewById(R.id.dogButton);
        gameOverText.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.GONE);
        catButton.setVisibility(View.VISIBLE);
        dogButton.setVisibility(View.VISIBLE);

        countdown();
    }

    private void countdown() {
        animalChosen = "";
        new CountDownTimer(3000, 1000) {
            String animal = getAnimal();
            boolean correctAnswer = false;
            TextView gameOverText = (TextView) findViewById(R.id.gameOverText);
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
                   Button startButton = (Button) findViewById(R.id.startButton);
                   Button catButton = (Button) findViewById(R.id.catButton);
                   Button dogButton = (Button) findViewById(R.id.dogButton);
                   gameOverText.setVisibility(View.VISIBLE);
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
                    Button startButton = (Button) findViewById(R.id.startButton);
                    Button catButton = (Button) findViewById(R.id.catButton);
                    Button dogButton = (Button) findViewById(R.id.dogButton);
                    gameOverText.setVisibility(View.VISIBLE);
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

    private Drawable getRandomAnimalImage(boolean isDog) {
        if(isDog) {
            // get random dog image
            return dogDrawables[rand.nextInt(dogDrawables.length)];
        } else {
            // get random cat image
            return catDrawables[rand.nextInt(catDrawables.length)];
        }
    }
}
