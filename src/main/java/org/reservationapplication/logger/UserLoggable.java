package org.reservationapplication.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UserLoggable {
    default Logger getUserLogger() {
        return LoggerFactory.getLogger("UserLogger");
    }
}