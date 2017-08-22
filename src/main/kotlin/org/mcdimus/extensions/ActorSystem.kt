package org.mcdimus.extensions

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

/**
 * @author Dmitri Maksimov
 */
inline fun <reified T : Any> ActorSystem.actor(name: String): ActorRef {
  return this.actorOf(Props.create(T::class.java), name)
}
