package com.gustavo.kmpdexlib.core.infra

import com.apollographql.apollo3.ApolloClient
import org.koin.dsl.module

val coreInfraModule = module {
    single {
        ApolloClient.Builder()
            .serverUrl("https://graphql.pokeapi.co/v1beta2")
            .build()
    }
}
