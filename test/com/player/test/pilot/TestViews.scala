package com.player.test.pilot

import play.test.Helpers.{ contentType, contentAsString }

import org.junit.Test
import org.fest.assertions.Assertions.assertThat

import views.html.index

class TestViews {

  @Test
  def test() {

    var html = views.html.simple("test")
    assertThat(contentType(html)).isEqualTo("text/html")

    val content = contentAsString(html)
    assertThat(content).contains("<title>test</title>")

  }

}