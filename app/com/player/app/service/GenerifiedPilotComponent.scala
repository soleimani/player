package com.player.app.service

import com.player.app.model.Pilot

trait GenerifiedPilotComponent extends com.player.app.service.GenericComponent[ Pilot ] {
  this: com.player.app.repository.api.Component[ Pilot ] ⇒
  object GenerifiedPilotService extends GenericService
}