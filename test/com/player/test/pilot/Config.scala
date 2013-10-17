package com.player.test.pilot

object Config {
  def fakeDB = Map( "player.generate" -> false )
  def inMemoryDatabase = play.api.test.Helpers.inMemoryDatabase() + ( "player.generate" -> false )
}