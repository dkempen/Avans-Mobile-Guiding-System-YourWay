package com.id.yourway.providers.listeners;

import com.id.yourway.entities.Sight;

import java.util.List;

public interface SightProviderListener {
    void onSightsAvailable(List<Sight> sights);
}
