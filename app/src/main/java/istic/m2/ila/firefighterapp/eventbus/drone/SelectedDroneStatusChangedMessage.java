package istic.m2.ila.firefighterapp.eventbus.drone;

import istic.m2.ila.firefighterapp.dto.EDroneStatus;

public class SelectedDroneStatusChangedMessage {
    private EDroneStatus _statut;
    //Constructor
    public SelectedDroneStatusChangedMessage(EDroneStatus statut, long id)
    {
        _statut = statut;
        _droneId = id;
    }

    private long _droneId;

    public long getDroneId() {
        return _droneId;
    }

    public EDroneStatus getDroneStatut() {
        return _statut;
    }
}
