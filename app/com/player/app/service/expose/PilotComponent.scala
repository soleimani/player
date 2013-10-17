package com.player.app.service.expose

trait PilotComponent extends com.player.app.service.PilotComponent
    with com.player.app.repository.PilotComponent {
  val repository = PilotRepository
}