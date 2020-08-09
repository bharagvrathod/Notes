package edu.harvard.cs50.notes;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.EditText;


public class NoteActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        editText = findViewById(R.id.note_edit_text);
        editText.setText(intent.getStringExtra("content"));
        editText.requestFocus();
        int position = editText.length();
        editText.setSelection(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        tintMenuIcon(NoteActivity.this, menu.findItem(R.id.delete), R.color.icons);
        tintMenuIcon(NoteActivity.this, menu.findItem(R.id.save), R.color.icons);
        return true;
    }

   private void tintMenuIcon(Context context, MenuItem item, @ColorRes int colour){
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(colour));
        item.setIcon(wrapDrawable);
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                deleteNote();
                return true;
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        MainActivity.database.noteDao().save(editText.getText().toString(), id);
    }

    private void saveNote() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id",0);
        new SaveNote().execute(id);
    }

    private void deleteNote() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);
        new DeleteNote().execute(id);
    }

    private class DeleteNote extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... longs) {
            MainActivity.database.noteDao().delete(longs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }

    private class SaveNote extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... longs) {
            MainActivity.database.noteDao().save(editText.getText().toString(), longs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity.reload();

        }
    }
}

