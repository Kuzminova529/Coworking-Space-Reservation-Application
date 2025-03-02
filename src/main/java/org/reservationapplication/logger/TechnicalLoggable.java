package org.reservationapplication.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TechnicalLoggable {
    default Logger getTechnicalLogger() {
        return LoggerFactory.getLogger("TechnicalLogger");
    }
}