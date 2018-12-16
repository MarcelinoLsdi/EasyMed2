package com.example.marcelino.easymed.Remote;

import com.example.marcelino.easymed.model.Collector;
import com.example.marcelino.easymed.model.DataHelper;
import com.example.marcelino.easymed.model.Resources;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FarmaciaService {

    @GET("catalog/resources/search")
    Call<Resources> buscarFarmaciaPeloMedicamento(@Query("capability") String Medicamento);

    @GET("catalog/resources/search")
    Call<Resources> buscarFarmaciaPeloMedicamentoEPosicao(@Query("capability") String Medicamento,
                                                          @Query("lat") Double lat,
                                                          @Query("lon") Double lon,
                                                          @Query("radius") int raio
    );

    @POST("collector/resources/data/last")
    Call<DataHelper> buscarUltimoDado(@Body Collector collector);

}
