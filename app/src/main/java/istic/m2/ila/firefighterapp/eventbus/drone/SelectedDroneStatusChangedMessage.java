package istic.m2.ila.firefighterapp.eventbus.drone;

import istic.m2.ila.firefighterapp.dto.EDroneStatut;

public class SelectedDroneStatusChangedMessage {
    private EDroneStatut _statut;
    public EDroneStatut getDroneStatut() { return _statut; }

    private long _droneId;
    public long getDroneId() { return _droneId; }

    //Constructor
    public SelectedDroneStatusChangedMessage(EDroneStatut statut, long id)
    {
        _statut = statut;
        _droneId = id;
    }
}
