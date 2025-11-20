package io.mityukov.simpla.core.data.repository

sealed interface RepositoryFailure {
    data object IO : RepositoryFailure
}
