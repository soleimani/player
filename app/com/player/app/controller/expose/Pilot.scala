package com.player.app.controller.expose

object Pilot extends com.player.app.controller.Pilot
    with com.player.app.service.expose.PilotComponent {
  val service = PilotService
}