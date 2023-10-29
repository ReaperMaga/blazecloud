package sh.maga.blazecloud.backend.exception;

import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class ServiceException extends Exception {

    private final int status;

    public ServiceException(int status, String message) {
        super(message);
        this.status = status;
    }
}
