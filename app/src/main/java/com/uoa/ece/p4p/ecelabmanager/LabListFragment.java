/*
* Name : LabListFragment.java
* Author : Tae-Woong Youn & Henry Lee
*  A Fragment represents a behavior or a portion of user interface in an Activity.
* This script file gives a behavior of user interface in an LabActivity.
* */


package com.uoa.ece.p4p.ecelabmanager;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uoa.ece.p4p.ecelabmanager.api.Course;
import com.uoa.ece.p4p.ecelabmanager.api.Server;
import com.uoa.ece.p4p.ecelabmanager.api.Student;
import com.uoa.ece.p4p.ecelabmanager.utility.GlobalState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Rawr on 29/07/2014.
 */
public class LabListFragment extends Fragment {

    String TAG = "Part4ProjectLabApplication";

    private LabListAdapter mLabListAdapter;

    private ArrayList<String> autoCompleteLibrary;
    private ListView listView;
    private AutoCompleteTextView autoCompleteStudent;

    private ActionProcessButton btnBarcodeScan;

    private ArrayAdapter<String> adapter;

    private final Fragment thisFragment = this;
    private Handler handler = new Handler();

    private TextView statusView;

    private SharedPreferences prefs;
    private String displayMethod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Init Variables
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        displayMethod = prefs.getString(getString(R.string.pref_display_method_key),getString(R.string.pref_display_method_default));

        String labName = GlobalState.getLab().name;
        String courseName = GlobalState.getLab().course;
        ((LabActivity) getActivity()).setActionBarTitle(labName + " - " + courseName.toUpperCase());
        mLabListAdapter = new LabListAdapter(getActivity());

        //Init auto-complete array and adapter
        autoCompleteLibrary = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, autoCompleteLibrary);

        new GetStudentListTask(courseName).execute();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lab_list, container, false);

        statusView = (TextView) rootView.findViewById(R.id.lab_list_status);

        // Set up auto-complete-enabled text view
        autoCompleteStudent = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteStudent);
        autoCompleteStudent.setAdapter(adapter);
        autoCompleteStudent.setThreshold(1);
        autoCompleteStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String input = (String) parent.getItemAtPosition(position);
//                ArrayList<QueryRow> results= new ArrayList<QueryRow>();
                if(android.text.TextUtils.isDigitsOnly(input)){
                    //auid
//                    Query query = auidQuery(input);
//                    try {
//                        results = couchbaseSetup.getRowsFromQueryEnumerator(query.run());
//                    } catch (CouchbaseLiteException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    name
//                    Query query = nameQuery(input);
//                    try {
//                        results = couchbaseSetup.getRowsFromQueryEnumerator(query.run());
//                    } catch (CouchbaseLiteException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(results != null && results.size() > 0){
//                    String studentId = results.get(0).getDocument().getId();
//                    Intent intent = new Intent(getActivity(), MarkingDialogActivity.class).putExtra(Intent.EXTRA_TEXT, studentId).putExtra(Intent.EXTRA_UID, (String) labDocument.getId());
//                    startActivity(intent);
                }
            }
        });

        listView = (ListView) rootView.findViewById(R.id.lab_list_student);
        listView.setAdapter(mLabListAdapter);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (queryResult != null) {
                    Log.i(TAG, "1 isStale " + String.valueOf(queryResult.isStale()));
                }

                LabListAdapter.Item item;
                String studentId;
                if (mLabListAdapter.getItem(i) instanceof LabListAdapter.Item) {
                    item = (LabListAdapter.Item) mLabListAdapter.getItem(i);
                    studentId = item.id;
                    Intent intent = new Intent(getActivity(), MarkingDialogActivity.class).putExtra(Intent.EXTRA_TEXT, (String) studentId).putExtra(Intent.EXTRA_UID, (String) labDocument.getId());
                    startActivity(intent);
                } else {
                    //Do nothing
                }
            }
        });
*/
        btnBarcodeScan = (ActionProcessButton)rootView.findViewById(R.id.ScanButton);
        btnBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forFragment(thisFragment).initiateScan();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    // When the barcode is scanned from the student's ID card, our application get the scanned result first.
    // Required result is the ID number of the student and it is contained in the scanned result.
    // The codes below are getting the ID number of the student.
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                    resultCode, data);
            if (scanResult == null) {
                return;
            }
            final String result = scanResult.getContents();
            if (result != null) {
                String id = result.substring(5, 12);
                Log.d(TAG, id);
/*
                        ArrayList<QueryRow> results = new ArrayList<QueryRow>();
                        // ID number is the substring of the string 'scanned result'
                        String barcodeString = result.substring(5, 12);
                        Query query = auidQuery(barcodeString);

                        try {
                            results = couchbaseSetup.getRowsFromQueryEnumerator(query.run());
                        } catch (CouchbaseLiteException e) {
                            e.printStackTrace();
                        }
                        if (results != null && results.size() > 0) {
                            String studentId = results.get(0).getDocument().getId();
                            Intent intent = new Intent(getActivity(), MarkingDialogActivity.class).putExtra(Intent.EXTRA_TEXT, studentId).putExtra(Intent.EXTRA_UID, (String) labDocument.getId());
                            startActivity(intent);
                        }else{
                            //If there is NO matching student ID number, it shows the toast message.
                            Toast.makeText(getActivity(),"Student not found",Toast.LENGTH_SHORT).show();
                        }
                        */
            }
        }
    }

    class GetStudentListTask extends AsyncTask<Void, Void, ArrayList<Student>> {
        private String course;
        private Throwable e;

        public GetStudentListTask(String course) {
            this.course = course;
        }

        private void updateAutoCompleteAdapter(ArrayList<Student> students){
            adapter.clear();
            for (Student s : students) {
                adapter.add(s.name);
                adapter.add(s.id);
            }
            adapter.notifyDataSetChanged();
        }

        private void updateLabListAdapter(ArrayList<Student> students) {
            ArrayList<LabListAdapter.Row> rows = new ArrayList<LabListAdapter.Row>();
            String previousLetter = "";
            for (Student s : students) {
                String firstLetter = s.name.substring(0, 1).toUpperCase(Locale.UK);
                if (!firstLetter.equals(previousLetter)) {
                    rows.add(new LabListAdapter.Section(firstLetter));
                    previousLetter = firstLetter;
                }
                rows.add(new LabListAdapter.Item(s));
            }
            mLabListAdapter.update(rows);
        }

        @Override
        protected ArrayList<Student> doInBackground(Void... voids) {
            try {
                ArrayList<Student> students = Server.get_student_list(course);
                Collections.sort(students, new Comparator<Student>() {
                    @Override
                    public int compare(Student student, Student student2) {
                        return student.name.compareTo(student2.name);
                    }
                });
                return students;
            } catch (Throwable e) {
                this.e = e;
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Student> students) {
            if (students == null) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Failed to get student list: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                updateLabListAdapter(students);
                updateAutoCompleteAdapter(students);
            }
        }
    }

}
