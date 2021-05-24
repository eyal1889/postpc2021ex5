package exercise.android.reemh.todo_items;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTodo  extends AppCompatActivity {
    EditText description_right;
    int pos;
    Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        TextView description_left = findViewById(R.id.description_left);
        TextView last_modified_left = findViewById(R.id.last_modified_left);
        TextView creation_date_left = findViewById(R.id.creation_date_left);
        TextView status_left = findViewById(R.id.status_left);

        description_left.setText("description:");
        last_modified_left.setText("last modified:");
        status_left.setText("status:");
        creation_date_left.setText("creation date:");

        description_right = findViewById(R.id.description_right);
        TextView last_modified_right = findViewById(R.id.last_modified_right);
        TextView creation_date_right = findViewById(R.id.creation_date_right);
        CheckBox status_right = findViewById(R.id.status_right);
        Intent createdMe = getIntent();
        boolean status=false;
        String description = "";
        String last_modified = "";
        String creation_date = "";
        if(createdMe.hasExtra("status")){
            status = createdMe.getBooleanExtra("status",false);
        }
        if(createdMe.hasExtra("description")){
            description = createdMe.getStringExtra("description");
        }
        if(createdMe.hasExtra("last_modified")){
            last_modified = createdMe.getStringExtra("last_modified");
        }
        if(createdMe.hasExtra("creation_date")){
            creation_date = createdMe.getStringExtra("creation_date");
        }
        if(createdMe.hasExtra("pos")){
            pos = createdMe.getIntExtra("pos",0);
        }
        description_right.setText(description);
        last_modified_right.setText(last_modified);
        creation_date_right.setText(creation_date);
        status_right.setChecked(status);

        description_right.addTextChangedListener(textWatcher);

        status_right.setOnClickListener(v -> {
            Intent bd = new Intent("checkbox_in_edit");
            bd.putExtra("check_status",status_right.isChecked());
            bd.putExtra("pos",pos);
            context.sendBroadcast(bd);
        });

    }
    private final TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            //todo send broadcast
            Intent bd = new Intent("db_changed");
            String str = description_right.getText().toString();
            bd.putExtra("new_desc",str);
            bd.putExtra("pos",pos);
            context.sendBroadcast(bd);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };
}
