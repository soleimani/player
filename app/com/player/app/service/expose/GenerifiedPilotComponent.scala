package com.player.app.service.expose

trait GenerifiedPilotComponent extends com.player.app.service.GenerifiedPilotComponent
    with com.player.app.repository.GenerifiedPilotComponent {
  val repository = GenerifiedPilotRepository
}