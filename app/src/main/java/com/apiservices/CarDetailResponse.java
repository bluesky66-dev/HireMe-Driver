package com.apiservices;

import java.util.List;

/**
 * Created by DELL on 11-Sep-17.
 */

public class CarDetailResponse extends BaseReponse {
    @ResponseAnotation("response")
    List<Car> response;
}
