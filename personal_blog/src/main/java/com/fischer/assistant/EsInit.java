package com.fischer.assistant;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EsInit {
    @Value("es.uri")
    private String esUri;
    public RestHighLevelClient init(){
        HttpHost httpHost=HttpHost.create(esUri);
        RestClientBuilder builder= RestClient.builder(httpHost);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
