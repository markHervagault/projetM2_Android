package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

import istic.m2.ila.firefighterapp.dto.EDroneStatut;

public class SelectedDroneStatusChangedMessage implements MessageBus
{
    private EDroneStatut _statut;
    public EDroneStatut getDroneStatut() { return _statut; }

    //Constructor
    public SelectedDroneStatusChangedMessage(EDroneStatut statut)
    {
        _statut = statut;
    }
}
