package com.example.mprojects.classplus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class assignment extends AppCompatActivity {
    EditText points,duedate,topic;
    TextInputLayout titlen,desc;
    DatePickerDialog dialog;
    public static final String UPLOAD_URL =Config.HOST+"assign_quest.php";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Declaring views
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    ProgressDialog progressDialog;
    String pdfName,pdfUrl;
    //an array to hold the different pdf objects
    ArrayList<Pdf> pdfList = new ArrayList<Pdf>();

    PdfAdapter pdfAdapter;
    SharedPreferences sp;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment);
        Toolbar toolbar=findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Assignment");

        titlen=findViewById(R.id.tname);
        desc=findViewById(R.id.desc);
        points=findViewById(R.id.points);
        duedate=findViewById(R.id.date);
        topic=findViewById(R.id.topic);

        sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);


        duedate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){

                Calendar calender = Calendar.getInstance();
                final int year = calender.get(Calendar.YEAR);
                final int month = calender.get(Calendar.MONTH);
                final int day = calender.get(Calendar.DAY_OF_WEEK);

                dialog = new DatePickerDialog(assignment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        duedate.setText(year + "-" + (month + 1) + "-" + day);

                    }
                }, year, month, day);
                ((DatePickerDialog) dialog).show();
                    return false;
            }

                return true;
            }


        });


    }@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.mattach){
            showFileChooser();
        }
        if(id==R.id.msure){
            uploadMultipart();
            Intent i=new Intent(assignment.this,classcreated.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //assignment attachment
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        //getting name for the image
        String name = topic.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("title",titlen.getEditText().getText().toString())
                        .addParameter("name",name)
                        .addParameter("desc",desc.getEditText().getText().toString())
                        .addParameter("points",points.getText().toString())
                        .addParameter("duedate",duedate.getText().toString())
                        .addParameter("email",sp.getString(Config.cemail,"Not found"))//Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
                Toast.makeText(this, "File Uploading "+ sp.getString(Config.cemail,"Not found"), Toast.LENGTH_SHORT).show();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    //method to show file chooser
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}