package com.outsystems.plugins.firebaseperformance;

import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.HashMap;
import java.util.Map;

public class OSFirebasePerformance extends CordovaPlugin {

    private final static String KEY_ACTION_START_TRACE = "startTrace";
    private final static String KEY_ACTION_STOP_TRACE = "stopTrace";
    private final static String KEY_ACTION_ADD_TRACE_ATTRIBUTE = "addTraceAttribute";
    private final static String KEY_ACTION_REMOVE_TRACE_ATTRIBUTE = "removeTraceAttribute";
    private final static String KEY_ACTION_INCREMENT_METRIC = "incrementMetric";
    private final static String KEY_ACTION_SET_PERFORMANCE_COLLECTION_ENABLED = "setPerformanceCollectionEnabled";    

    private Map<String, Trace> traces = new HashMap<String, Trace>();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(KEY_ACTION_START_TRACE)) {
            this.startTrace(args, callbackContext);
            return true;
        }
        else if (action.equals(KEY_ACTION_STOP_TRACE)) {
            this.stopTrace(args, callbackContext);
            return true;
        }
        else if (action.equals(KEY_ACTION_ADD_TRACE_ATTRIBUTE)) {
            this.addTraceAttribute(args, callbackContext);
            return true;
        }
        else if (action.equals(KEY_ACTION_REMOVE_TRACE_ATTRIBUTE)) {
            this.removeTraceAttribute(args, callbackContext);
            return true;
        }
        else if (action.equals(KEY_ACTION_INCREMENT_METRIC)) {
            this.incrementMetric(args, callbackContext);
            return true;
        }
        else if (action.equals(KEY_ACTION_SET_PERFORMANCE_COLLECTION_ENABLED)) {
            this.setPerformanceCollectionEnabled(args, callbackContext);
            return true;
        }
        return false;
    }

    private void startTrace(JSONArray args, CallbackContext callbackContext) {
        String traceName = null;
        Trace trace = null;
        try {
            traceName = args.getString(0);
        } catch (JSONException e) {
            Log.e("StartTrace", e.toString());
            return;
        }
        if(!traceName.isEmpty()){
            if(traces.containsKey(traceName)){
                trace = traces.get(traceName);
                Log.e("FirebasePerformance_1", traceName);
            }
            else{
                trace = FirebasePerformance.getInstance().newTrace(traceName);
                traces.put(traceName, trace);
                Log.e("FirebasePerformance_2", traceName);
            }
            trace.start();
        }
    }
  
    private void stopTrace(JSONArray args, CallbackContext callbackContext) {
        String traceName = null;
        try {
            traceName = args.getString(0);
        } catch (JSONException e) {
            Log.e("StopTrace", e.toString());
            return;
        }
        Log.e("FirebasePerformance_stop", traceName);
        Trace trace = traces.get(traceName);
        trace.stop();
        
        try {
            Trace trace1 = traces.get(traceName);
        } catch () {
            Log.e("FirebasePerformance_stop_2", traceName);
            return;
        }
    }

    private void addTraceAttribute(JSONArray args, CallbackContext callbackContext) {
        String traceName = null;
        String attributeName = null;
        String value = null;
        Trace trace = null;
        try {
            traceName = args.getString(0);
            attributeName = args.getString(1);
            value = args.getString(2);
        } catch (JSONException e) {
            Log.e("AddTraceAttribute", e.toString());
            return;
        }
        if(!traceName.isEmpty() && !attributeName.isEmpty() && !value.isEmpty()){
            if(traces.containsKey(traceName)){
                trace = traces.get(traceName);
                trace.putAttribute(attributeName, value);
            }
        }
    }

    private void removeTraceAttribute(JSONArray args, CallbackContext callbackContext) {
        String traceName = null;
        String attributeName = null;
        Trace trace = null;
        try {
            traceName = args.getString(0);
            attributeName = args.getString(1);
        } catch (JSONException e) {
            Log.e("RemoveTraceAttribute", e.toString());
            return;
        }
        if(!traceName.isEmpty() && !attributeName.isEmpty()){
            if(traces.containsKey(traceName)){
                trace = traces.get(traceName);
                trace.removeAttribute(attributeName);
            }
        }
    }

    private void incrementMetric(JSONArray args, CallbackContext callbackContext) {
        String traceName = null;
        String metricName = null;
        Long value = null;
        try {
            traceName = args.getString(0);
            metricName = args.getString(1);
            value = args.getLong(2);
        } catch (JSONException e) {
            Log.e("IncrementMetric", e.toString());
            return;
        }
        Trace trace = traces.get(traceName);
        trace.incrementMetric(metricName, value);
    }

    private void setPerformanceCollectionEnabled(JSONArray args, CallbackContext callbackContext) {
        Boolean enabled = null;
        try {
            enabled = args.getBoolean(0);
        } catch (JSONException e) {
            Log.e("SetEnabled", e.toString());
            return;
        }
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(enabled);
    }
}
