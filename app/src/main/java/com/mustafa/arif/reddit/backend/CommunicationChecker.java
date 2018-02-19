package com.mustafa.arif.reddit.backend;

/**
 * Class to check all sort of commuication
 */
public interface CommunicationChecker {

    /**
     * Returns the status of network connection. Provides the current status immediately.
     *
     * @return {@code true} if connection is available, {@code false}.
     */
    boolean isNetworkAvailable();
}
