package pl.ejdev.spotifyplugin.api.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.http.HttpClient

// TODO dependency injection
val objectMapper: ObjectMapper = jacksonObjectMapper()
val httpClient: HttpClient = HttpClient.newHttpClient()