package com.nataliatsi.mavis.exception;

import java.util.List;

public record ApiError(
        int status,
        String message,
        List<String> erros
) {
}
