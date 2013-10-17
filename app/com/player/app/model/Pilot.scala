package com.player.app.model
 
import anorm.Pk

case class Pilot(id: Pk[Long], name: String) extends Model