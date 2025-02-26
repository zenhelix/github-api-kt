package io.github.zenhelix.github.client.http.license

import io.github.zenhelix.github.client.http.model.ErrorResponse
import io.github.zenhelix.github.client.http.model.HttpResponseResult
import io.github.zenhelix.github.client.http.model.LicensesResponse

public interface GithubLicensesApi {

    public fun licenses(token: String? = null): HttpResponseResult<LicensesResponse, ErrorResponse>

}