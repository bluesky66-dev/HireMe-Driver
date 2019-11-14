package com.apiservices;

/**
 * Created by grepixinfotech on 09/09/17.
 */

public class AppBaseRequest extends BaseRequest {
    @AppAnotation("api_key")
    static String apiKey;
    static {
        apiKey=AuthData.getInstance().getApiKey();
    }
}
