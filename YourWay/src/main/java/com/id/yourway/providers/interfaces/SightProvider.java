package com.id.yourway.providers.interfaces;

import com.id.yourway.providers.listeners.SightProviderListener;

public interface SightProvider {
    void getSights(SightProviderListener listener);
}
