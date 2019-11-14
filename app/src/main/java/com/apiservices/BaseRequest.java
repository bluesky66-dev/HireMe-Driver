package com.apiservices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by grepixinfotech on 08/09/17.
 */

public class BaseRequest {
    public interface OnComplete {
        void onSuccess();

        void onSuccessWithResponse(BaseReponse classd);

        void onError();

        void onException(Exception e);
    }

    public interface OnCompleteFinal<VH extends BaseReponse> {
        void onSuccess();

        void onSuccessWithResponse(VH response);

        void onError();

        void onException(Exception e);
    }

    public interface OnSuccessListener<VH extends BaseReponse> {
        void onSuccess(VH response);
    }

    public interface OnExceptionListener<VH extends BaseReponse> {
        void onException(Exception e);
    }

    public interface OnErrorListener<VH extends BaseReponse> {
        void onError(VolleyError e);
    }

    public BaseReponse baseReponse;

    private Error error;
    protected HashMap<String, String> hashMap = new HashMap<>();

    private Field response;
    private String apiNamea;

    public void prepair() {
        Class class1 = this.getClass();
        for (Field filed : class1.getFields()) {
            AppAnotation annotation = filed.getAnnotation(AppAnotation.class);
            if (annotation != null) {

                try {
                    Object object = filed.get(this);
                    if (object != null) {
                        hashMap.put(annotation.value(), "" + object);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            ResponseAnotation rannotation = filed.getAnnotation(ResponseAnotation.class);
            if (rannotation != null) {
                response = filed;
            }

        }


        ApiName apiName = (ApiName) class1.getAnnotation(ApiName.class);
        if (apiName != null) {
            apiNamea = "" + apiName.value();
        }
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }



    public void execute(final Context context, final OnSuccessListener onComplete) {
        execute(context, onComplete, null, null);
    }

    public void execute(final Context context, final OnSuccessListener onComplete, final OnErrorListener errorListener) {
        execute(context, onComplete, null, errorListener);
    }

    public void execute(final Context context, final OnSuccessListener onComplete, final OnExceptionListener exceptionListener, final OnErrorListener errorListener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  apiNamea,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            Type[] genericInterfaces = onComplete.getClass().getGenericInterfaces();
                            Type type = genericInterfaces[0];
                            ParameterizedType parameterizedType = (ParameterizedType) type;
                            Type[] actualTypeArguments = parameterizedType
                                    .getActualTypeArguments();
                            String name = actualTypeArguments[0].toString();
                            try {
                                Class responseClass = Class.forName(name.replace("class ", ""));
                                Object baseReponseC = responseClass.newInstance();
                                JSONObject jsonObject = new JSONObject(response1);
                                BaseReponse baseReponse = (BaseReponse) baseReponseC;
                                for (Field filed : responseClass.getFields()) {
                                    AppAnotation annotation = filed.getAnnotation(AppAnotation.class);
                                    if (annotation != null) {
                                        filed.set(baseReponseC, jsonObject.getString(annotation.value()));
                                    }
                                    ResponseAnotation rannotation = filed.getAnnotation(ResponseAnotation.class);
                                    if (rannotation != null) {
                                        filed.set(baseReponseC, baseReponse.parse(jsonObject.get(rannotation.value()).toString(), filed.getType()));
                                    }

                                }
                                onComplete.onSuccess(baseReponse);
                            } catch (ClassNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (exceptionListener != null) {
                                    exceptionListener.onException(e);
                                }
                            } catch (InstantiationException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (exceptionListener != null) {
                                    exceptionListener.onException(e);
                                }
                            } catch (IllegalAccessException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (exceptionListener != null) {
                                    exceptionListener.onException(e);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (exceptionListener != null) {
                                exceptionListener.onException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error1) {
                        if (errorListener != null) {
                            errorListener.onError(error1);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getHashMap();
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static <E> void printArray(E[] inputArray) {
        // Display array elements
        for (E element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

}
