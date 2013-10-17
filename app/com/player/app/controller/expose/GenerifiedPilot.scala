package com.player.app.controller.expose

object GenerifiedPilot extends com.player.app.controller.GenerifiedPilot
    with com.player.app.service.expose.GenerifiedPilotComponent {
  val service = GenerifiedPilotService
}