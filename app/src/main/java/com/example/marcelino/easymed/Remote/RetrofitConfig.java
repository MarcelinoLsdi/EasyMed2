package com.example.marcelino.easymed.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;
    public static final String BASE_URL = "http://cidadesinteligentes.lsdi.ufma.br/";

    public RetrofitConfig(){
        this.retrofit = new Retrofit.Builder()
                       .baseUrl(BASE_URL)
                       .addConverterFactory(JacksonConverterFactory.create())
                       .build();
    }

    public MedicamentoService getMedicamento(){
        return this.retrofit.create(MedicamentoService.class);
    }

    public FarmaciaService getFarmacia(){
        return this.retrofit.create(FarmaciaService.class);
    }
}
