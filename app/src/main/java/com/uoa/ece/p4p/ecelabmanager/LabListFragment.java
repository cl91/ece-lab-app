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

import java.util.ArrayList;
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

    private String activeLab = "";
    private String courseKey;
    private ArrayAdapter<String> adapter;

    private final Fragment thisFragment = this;
    private Handler  handler = new Handler();

    private String activeUser;
    private ScheduledExecutorService scheduler;
    private TextView statusView;

    private SharedPreferences prefs;
    private String displayMethod;


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        //Check if display method preference has been changed
        /*
        if(!displayMethod.equals(prefs.getString(getString(R.string.pref_display_method_key),getString(R.string.pref_display_method_default)))){
            queryResult = null;
        }
        //Start sceduler to check if any changes have been made
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @SuppressWarnings("ResourceType")
            @Override
            public void run() {

                //Get premade query, and check if stale
                Query query = getQuery();
                try {
                    if(queryResult == null||queryResult.isStale()){
                        if(queryResult != null) {
                            Log.i(TAG, "queryResult isStale = " + String.valueOf(queryResult.isStale()));
                        }
                        QueryEnumerator newResult = query.run();
                        queryResult = newResult;
                        processQueryEnumerator(newResult);
                    }
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                //Check replication status from CouchbaseSetup Class
                if(replicationStatus==null||!replicationStatus.equals(couchbaseSetup.getReplicationStatus())){

                    //Display Replication Status
                    replicationStatus = couchbaseSetup.getReplicationStatus();
                    String statusText;
                    int statusColor = getResources().getColor(R.color.blue_normal);
                    if(replicationStatus.equals(Replication.ReplicationStatus.REPLICATION_IDLE)){
                        statusText = "Local Changes in-sync with Server";
                        statusColor = getResources().getColor(R.color.blue_normal);
                    }else if(replicationStatus.equals(Replication.ReplicationStatus.REPLICATION_ACTIVE)){
                        statusText = "Syncing Changes to/from server";
                        statusColor = getResources().getColor(R.color.green_complete);
                    }else{
                        statusText = "Connection Unavaliable - Changes are local until connection reestablished";
                        statusColor = getResources().getColor(R.color.ECEOrange);
                    }

                    final String finalStatusText = statusText;
                    final int finalStatusColor = statusColor;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusView.setText(finalStatusText);
                            statusView.setBackgroundColor(finalStatusColor);
                        }
                    });
                }

            }
        },0,1, TimeUnit.SECONDS);
        */
        autoCompleteStudent.setText("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Init Variables
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            activeLab = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        displayMethod = prefs.getString(getString(R.string.pref_display_method_key),getString(R.string.pref_display_method_default));
/*
        labDocument =  couchbaseSetup.getDatabaseAdmin().getDocument(activeLab);
        courseKey = (String) labDocument.getProperty("courseKey");

        //Get Course Document from Database
        Document courseDocument =  couchbaseSetup.getDatabaseAdmin().getDocument("course::"+courseKey);
        String labName = (String) labDocument.getProperty("name");
        String courseName = (String) courseDocument.getProperty("name");
        //((LabActivity)getActivity()).setActionBarTitle(courseName+" - "+labName);
        ((LabActivity)getActivity()).setActionBarTitle(labName+" - "+courseName);
        mLabListAdapter = new LabListAdapter(getActivity(),null);

        //Init Autocomplete array and adapter
        autoCompleteLibrary = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,autoCompleteLibrary);

        activeUser = couchbaseSetup.getActiveUser();
        try {
            updateAutoCompleteAdapter(getQuery().run());
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //Stop scheduler if activity loses focus
        scheduler.shutdown();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lab_list, container, false);

        statusView = (TextView) rootView.findViewById(R.id.lab_list_status);

        //Our application support autocomplete function, the following codes are make its function to operate.
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

        btnBarcodeScan = (ActionProcessButton)rootView.findViewById(R.id.ScanButton);
        btnBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forFragment(thisFragment).initiateScan();
            }
        });
*/
        return rootView;
    }
/*
    //Function to create a query for student names sorted by course and looks for a specific key value name.
    private Query nameQuery(String name){
        String key = name;
        Query query = couchbaseSetup.getStudentNameByCourse().createQuery();
        ArrayList<Object> startKey = new ArrayList<Object>();
        startKey.add(courseKey);
        startKey.add(key);
        ArrayList<Object> endKey = new ArrayList<Object>();
        endKey.add(courseKey);
        endKey.add(key);
        query.setStartKey(startKey);
        query.setEndKey(endKey);
        return query;
    }

    //Function to create a query for student auids sorted by course and looks for a specific key value auid
    private Query auidQuery(String auid){
        String key = auid;
        Query query = couchbaseSetup.getStudentAUIDByCourse().createQuery();
        ArrayList<Object> startKey = new ArrayList<Object>();
        startKey.add(courseKey);
        startKey.add(key);
        ArrayList<Object> endKey = new ArrayList<Object>();
        endKey.add(courseKey);
        endKey.add(key);
        query.setStartKey(startKey);
        query.setEndKey(endKey);
        return query;
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    // When the barcode is scanned from the student's ID card, our application get the scanned result first.
    // Required result is the ID number of the student and it is contained in the scanned result.
    // The codes below are getting the ID number of the student.
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    break;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    if(result.length() == 14) { //All of the scanned results are 14 digits long.
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
                break;
            default:
        }
    }

    //Obselete, previously used instead of scheduler but ran into issues regarding latency and ConnectionTimeout Exception
    private void restartLiveQuery() {
        /*
        if (mLiveQuery != null) {
            Log.i(TAG,"mLiveQuery stop");
            mLiveQuery.stop();
        }
        mLiveQuery = getLiveQuery();
        Log.i(TAG,"mLiveQuery getLiveQuery");
        mLiveQuery.start();
        Log.i(TAG,"mLiveQuery start");
        */
    }
/*
    //Function to create a query to returns all the students sorted by Course then Name
    private Query getQuery(){
        com.couchbase.lite.View studentNameByCourse = couchbaseSetup.getStudentNameByCourse();
        Query query = studentNameByCourse.createQuery();

        ArrayList<Object> startKey = new ArrayList<Object>();
        startKey.add(courseKey);
        startKey.add(null);
        startKey.add(null);
        ArrayList<Object> endKey = new ArrayList<Object>();
        endKey.add(courseKey);
        endKey.add(new HashMap<String, Object>());
        endKey.add(new HashMap<String, Object>());
        query.setStartKey(startKey);
        query.setEndKey(endKey);

        return query;
    }

    private LiveQuery getLiveQuery(){

        LiveQuery query = getQuery().toLiveQuery();

        query.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(final LiveQuery.ChangeEvent event) {
                //If needed, start Process dialog in computational task
                if(event.getSource().equals(mLiveQuery)) {
                    Log.i(TAG, "changedEvent Fired");
                    processQueryEnumerator(event.getRows());

                }
            }
        });

        return query;
    }

    private void processQueryEnumerator(QueryEnumerator enumerator){


        List<LabListAdapter.Row> rows = new ArrayList<LabListAdapter.Row>();

        String previousLetter = null;


        for (Iterator<QueryRow> it = enumerator; it.hasNext(); ) {
            QueryRow row = it.next();

            Map student = (Map) row.getValue();

            HashMap<String, HashMap<String, HashMap<String,String>>> labMarks = (HashMap<String, HashMap<String, HashMap<String, String>>>) student.get("labMarks");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String displayMethod = prefs.getString(getString(R.string.pref_display_method_key),getString(R.string.pref_display_method_default));

            if(displayMethod.equals("0")){
                //Full Sync List
            }else{
                //Local History List
                if(!(labMarks.containsKey(activeLab) && labMarks.get(activeLab).containsKey(activeUser))){
                    continue;
                }
            }

            String studentName = (String) student.get("name");
            String studentAuid = (String) student.get("auid");
            String studentKey = row.getDocumentId();

            String firstLetter = studentName.substring(0, 1).toUpperCase(Locale.UK);



            // Check if we need to add a header row
            if (!firstLetter.equals(previousLetter)) {
                rows.add(new LabListAdapter.Section(firstLetter));
//                sections.put(firstLetter, start);
            }

            String isMarked = "";
            if(!labMarks.isEmpty() && labMarks.containsKey(activeLab)){
                //This line only checks if the labHas been marked by TAs.

                long latestTime = 0;
                for(Map.Entry<String, HashMap<String,String>> userMap : labMarks.get(activeLab).entrySet()){
                    HashMap<String,String> record = userMap.getValue();
                    long timestamp = Long.parseLong(record.get("time"));
                    if(timestamp > latestTime){
                        latestTime = timestamp;
                        isMarked = record.get("mark");
                    }
                }

                // Originally, our application implemented to show '-' and '✓' when the student is marked off.
                // However, there was a feedback of both indications are not clearly visible, thus we changed to show
                // green background when student is marked.
                if(isMarked.equals("-1")){
                    isMarked = "-";
                }else if(isMarked.equals("0")){

                    isMarked = "✓";
                }else{
                    isMarked = "✓";
                }
            }else{
                isMarked = "-";
            }
            rows.add(new LabListAdapter.Item(studentName,studentAuid, isMarked, studentKey));
            previousLetter = firstLetter;

        }

        mLabListAdapter.update(rows);
        Log.i(TAG,"changedEvent Finished");

    }

    private void updateAutoCompleteAdapter(QueryEnumerator enumerator){
        adapter.clear();
        for (Iterator<QueryRow> it = enumerator; it.hasNext(); ) {
            QueryRow row = it.next();
            Document student = row.getDocument();
            String studentName = (String) student.getProperty("name");
            String studentAuid = (String) student.getProperty("auid");
            adapter.add(studentName);
            adapter.add(studentAuid);
        }
        adapter.notifyDataSetChanged();
    }
    */
}
