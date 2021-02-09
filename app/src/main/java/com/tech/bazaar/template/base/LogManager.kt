package com.tech.bazaar.template.base

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogManager(tag: String) {

    private val logger: Logger = LoggerFactory.getLogger(tag)

    fun d(message: String?) {
        logger.debug(message)
    }

    fun i(message: String?) {
        logger.info(message)
    }

    fun e(throwable: Throwable?) {
        logger.error("An error has occurred", throwable)
    }

    fun e(message: String?, throwable: Throwable?) {
        logger.error(message, throwable)
    }

    fun e(message: String?) {
        logger.error(message)
    }

    fun w(message: String?, exception: Exception?) {
        logger.warn(message, exception)
    }

    fun w(message: String?) {
        logger.warn(message)
    }

    companion object {
        fun getInstance(tag: String): LogManager {
            return LogManager(tag)
        }
    }

}