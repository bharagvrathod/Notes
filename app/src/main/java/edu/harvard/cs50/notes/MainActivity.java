package edu.harvard.cs50.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static NotesAdapter adapter;
    public static NotesDatabase database;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room
            .databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes")
            .allowMainThreadQueries()
            .build();

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new NotesAdapter();
        coordinatorLayout = findViewById(R.id.coordinator_layout);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.add_note_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = database.noteDao().create(item.content);
                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("content", database.noteDao().getcontent(id));

                view.getContext().startActivity(intent);
                reload();
            }
        });
    enableSwipeToDeleteCallBackAndUndo();
    }


    @Override
    protected void onResume() {
        super.onResume();

        reload();
    }

    public static void reload() {
        adapter.reload();
    }

    private void enableSwipeToDeleteCallBackAndUndo(){
        SwipeToDeleteCallBack swipeToDeleteCallBack = new SwipeToDeleteCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Note item = adapter.getNote(position);

                adapter.removeItem(position, item.id);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Note was deleted.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                 }
                );

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallBack);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
