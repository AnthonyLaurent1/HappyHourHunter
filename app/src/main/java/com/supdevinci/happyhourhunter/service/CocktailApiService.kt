package com.supdevinci.happyhourhunter.service

import com.supdevinci.happyhourhunter.model.CocktailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService {

    @GET("filter.php")
    suspend fun getCocktailsByCategory(
        @Query("c") category: String
    ): CocktailResponse

    @GET("lookup.php")
    suspend fun getCocktailDetail(
        @Query("i") id: String
    ): CocktailResponse

    @GET("search.php")
    suspend fun searchCocktailsByName(
        @Query("s") name: String
    ): CocktailResponse

}