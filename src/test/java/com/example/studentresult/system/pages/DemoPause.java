package com.example.studentresult.system.pages;

final class DemoPause {

    private static final long DEFAULT_DELAY_MS = 1000L;

    private DemoPause() {
    }

    static void pause() {
        String configuredDelay = System.getenv().getOrDefault(
                "SELENIUM_DEMO_DELAY_MS",
                String.valueOf(DEFAULT_DELAY_MS)
        );

        final long delay;
        try {
            delay = Long.parseLong(configuredDelay);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("SELENIUM_DEMO_DELAY_MS must be a whole number", exception);
        }

        if (delay < 0) {
            throw new IllegalArgumentException("SELENIUM_DEMO_DELAY_MS must not be negative");
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Selenium demo pause was interrupted", exception);
        }
    }
}
