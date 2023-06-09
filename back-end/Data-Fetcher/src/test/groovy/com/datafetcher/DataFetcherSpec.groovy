package com.datafetcher

import io.micronaut.http.HttpRequest
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject

@MicronautTest
class DataFetcherSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'test it works'() {
        when:
        HttpRequest request = HttpRequest.GET('/download/files')
        then:
        application.running
    }

}
