package com.example.kinozapis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class AddFilm extends AppCompatActivity {

    EditText titleBox;
    EditText genreBox;
    EditText scoreBox;
    EditText descriptionBox;
    Button delButton;
    Button saveButton;


    private DatabaseAdapter adapter;
    private long filmId=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        titleBox = (EditText) findViewById(R.id.Title);
        genreBox = (EditText) findViewById(R.id.Genre);
        scoreBox = (EditText) findViewById(R.id.Score);
        descriptionBox = (EditText) findViewById(R.id.Description);
        delButton = (Button) findViewById(R.id.DeleteButton);
        saveButton = (Button) findViewById(R.id.SaveButton);
        adapter = new DatabaseAdapter(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filmId = extras.getLong("id");
        }
        // если 0, то добавление
        if (filmId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            Film film = adapter.getFilm(filmId);
            titleBox.setText(film.getTitle());
            genreBox.setText(film.getGenre());
            scoreBox.setText(String.valueOf(film.getScore()));
            descriptionBox.setText(film.getDescription());
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }



    public String genreChecker(String test) {
        if (test.equals("")) {
            return "Введите жанр";
        }
        if (!test.matches("[а-яА-Яa-zA-z]+")) {
            return "Вводите только текст";
        }
        return null;
    }


    public String titleChecker(String test) {
        if (test.equals("")) {
            return "Введите название";
        }
        if (test.length() > 20) {
            return "Длинное название";
        }
        return null;
    }


    public String scoreChecker(String test) {
        int value = Integer.parseInt(test);
        if (value > 10) {
            return "Максимальная оценка: 10";
        }
        return null;
    }



    public void save(View view) {

        if ((genreChecker(genreBox.getText().toString()) != null) && titleChecker(titleBox.getText().toString()) != null) {
            Toast toast = Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_LONG);
            toast.show();
        }
        else if ((genreChecker(genreBox.getText().toString()) != null)) {
            Toast toast = Toast.makeText(this, genreChecker(genreBox.getText().toString()), Toast.LENGTH_LONG);
            toast.show();
        }else if (titleChecker(titleBox.getText().toString()) != null) {
            Toast toast = Toast.makeText(this, titleChecker(titleBox.getText().toString()), Toast.LENGTH_LONG);
            toast.show();
        }else {
            String title = titleBox.getText().toString();
            String genre = genreBox.getText().toString();
            int score = Integer.parseInt(scoreBox.getText().toString());
            String description = descriptionBox.getText().toString();
            Film film = new Film(filmId, title, genre, score, description);

            adapter.open();
            if (filmId > 0) {
                adapter.update(film);
            } else {
                adapter.insert(film);
            }
            adapter.close();
            goHome();
        }
    }


    public void delete(View view){
        adapter.open();
        adapter.delete(filmId);
        adapter.close();
        goHome();
    }


    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
