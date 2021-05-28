package exercise.android.reemh.todo_items;
import android.app.AppComponentFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


import androidx.appcompat.app.AppCompatActivity;

public class EditTodo  extends AppCompatActivity {
    EditText description_right;
    int pos;
    Context context = this;
    private BroadcastReceiver broadcastReceiverForCheckBox = null;

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
        boolean status = false;
        String description = "";
        String last_modified = "";
        String creation_date = "";
        String last="date";
        if (createdMe.hasExtra("status")) {
            status = createdMe.getBooleanExtra("status", false);
        }
        if (createdMe.hasExtra("description")) {
            description = createdMe.getStringExtra("description");
        }
        if (createdMe.hasExtra("last_modified")) {
            last_modified = createdMe.getStringExtra("last_modified");
            last= LastModified(last_modified);
        }

        if (createdMe.hasExtra("creation_date")) {
            creation_date = createdMe.getStringExtra("creation_date");
        }
        if (createdMe.hasExtra("pos")) {
            pos = createdMe.getIntExtra("pos", 0);
        }
        description_right.setText(description);
        if (last.equals("date")){
            last_modified_right.setText(last_modified);
        }
        else{
            last_modified_right.setText(last);
        }
        creation_date_right.setText(creation_date);
        status_right.setChecked(status);

        description_right.addTextChangedListener(textWatcher);

        broadcastReceiverForCheckBox = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent incomingIntent) {
                if (incomingIntent == null || !incomingIntent.getAction().equals("status_changed"))
                    return;
                pos = incomingIntent.getIntExtra("new_pos", 0);
            }
        };
        registerReceiver(broadcastReceiverForCheckBox, new IntentFilter("status_changed"));

        status_right.setOnClickListener(v -> {
            Intent bd = new Intent("checkbox_in_edit");
            bd.putExtra("check_status", status_right.isChecked());
            bd.putExtra("pos", pos);
            context.sendBroadcast(bd);
        });
//todo change pos when status changed!
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            Intent bd = new Intent("db_changed");
            String str = description_right.getText().toString();
            bd.putExtra("new_desc", str);
            bd.putExtra("pos", pos);
            context.sendBroadcast(bd);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverForCheckBox);

    }

    private String LastModified(String last_modified) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss .SSS");
        Date date = new Date(System.currentTimeMillis());
        String curr = formatter.format(date);
        String curr_year = curr.substring(0, 4);
        String last_modified_year = curr.substring(0, 4);

        if (Integer.parseInt(curr_year) - Integer.parseInt(last_modified_year) <= 0) {
            String curr_month = curr.substring(5, 7);
            String last_modified_month = last_modified.substring(5, 7);
            if (Integer.parseInt(curr_month) - Integer.parseInt(last_modified_month) <= 0) {
                String curr_day = curr.substring(8, 10);
                String last_modified_day = last_modified.substring(8, 10);
                if (Integer.parseInt(curr_day) - Integer.parseInt(last_modified_day) <= 0) {
                    float time_last_modified = Float.parseFloat(last_modified.substring(14, 16))
                            + Float.parseFloat(last_modified.substring(17, 19))/60;
                    float time_curr = Float.parseFloat(curr.substring(14, 16)) +
                            Float.parseFloat(curr.substring(17, 19)) / 60;
                    if (time_curr - time_last_modified <= 1) {
                        return String.valueOf(Math.round((time_curr - time_last_modified) * 60)) + " minutes ago";
                    } else {
                        return "Today at " + last_modified.substring(14, 22);
                    }
                }
            }
        }
        return "date";
    }
}
