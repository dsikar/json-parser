package com.example.android.jsonparser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by danielsikar on 11/03/2016.
 */
public class JsonFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_json, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String strJson = null;
        JsoknitObject jsoknit = null; // = new JsoknitObject(();)
        try {
            jsoknit = getFragmentWidgetsFromJson();
        }
        catch (JSONException e) {
            e.printStackTrace();
            strJson = e.getMessage();
            Toast.makeText(getContext(), strJson, Toast.LENGTH_SHORT).show();
        }

        getJsonLayout(view, jsoknit);
    }

    public void getJsonLayout(View view, final JsoknitObject jsoknit) {

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = v.getId();
                String call = jsoknit.getCall(id);
                // Bluetooth/Wifi "call" goes here
                System.out.println(call);
            }
        };

        // Add more listeners here

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.linearLayoutID);
        layout.setOrientation(LinearLayout.VERTICAL);

        try {
            for(int i = 0; i < jsoknit.functions.size(); i++) {
                JsoknitObject.Function func = jsoknit.functions.get(i);

                LinearLayout row = new LinearLayout(getContext());
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                switch(func.type) {
                    case "button":
                        Button btnTag = new Button(getContext());
                        btnTag.setOnClickListener(listener);
                        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        int vId = View.generateViewId();
                        btnTag.setId(vId);
                        btnTag.setText(func.label);
                        func.androidWidgetId = vId;
                        row.addView(btnTag);
                        // other cases go here, noting that as library expands, import statements need to be added to match new objects
                }
                layout.addView(row);
            }
        }
        catch (Exception e) {;
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            // TODO 1. Tell user JSON is not good. 2. Send string to json validation site. 3. Exit gracefully.
        }

    }

   private JsoknitObject getFragmentWidgetsFromJson() throws JSONException {

       // These are the names of the JSON objects that need to be extracted.
       final String LIBDEF = "libdef";
       final String DEF = "def";
       final String APP = "app";
       final String FUNCTION = "function";
       final String LABEL = "label";
       final String TYPE = "type";
       final String CALL = "call";

       String strJson = getResources().getString(R.string.json_data);
       JSONObject objJson = new JSONObject(strJson);
       JSONObject libDef = objJson.getJSONObject(LIBDEF);
       String libdef = libDef.getString(DEF);

       String app = libDef.getString(APP);

       JSONArray functions = objJson.getJSONArray(FUNCTION);
       JsoknitObject jkObj = new JsoknitObject(libdef, app);
       for(int i = 0; i < functions.length(); i++) {
           int id;
           String label;
           String type;
           String call;
           JSONObject function = functions.getJSONObject(i);
           id = function.getInt("id");
           label = function.getString("label");
           type = function.getString("type");
           call = function.getString("call");
           jkObj.addFunction(id, label, type, call);
       }
       return jkObj;
    }

   class JsoknitObject {
       public String libdef;
       public String app;
       public ArrayList<Function> functions = new ArrayList<Function>();
       public JsoknitObject(String sDef, String sApp){
           libdef = sDef;
           app = sApp;
       }
       public void addFunction(int id, String label, String type, String call) {
           Function function = new Function();
           function.add(id, label, type, call);
           functions.add(function);
       }

       public String getCall(int id) {
           Function func = null;
           for(int i = 0; i < functions.size(); i++) {
               func = functions.get(i);
               if(func.androidWidgetId == id) {
                   return func.call;
               }
           }
           return "Call not found";
       }

       class Function {
           public int id;
           public int androidWidgetId;
           public String label;
           public String type;
           public String call;
           public void add(int _id, String _label, String _type, String _call) {
               id = _id;
               label = _label;
               type = _type;
               call = _call;
           }
       }
    }
}



