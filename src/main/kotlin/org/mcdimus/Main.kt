package org.mcdimus

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import org.mcdimus.HelloActor.Goodbye
import org.mcdimus.HelloActor.Greeting
import org.mcdimus.extensions.match

class Main {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      println("Hello, World")
      val system = ActorSystem.create("MyActorSystem")
      val helloActor = system.actorOf(Props.create(HelloActor::class.java), "first-actor")
      helloActor.tell(Greeting("Hello", "Dmitri"), ActorRef.noSender())
      helloActor.tell(Goodbye("Bye"), ActorRef.noSender())

      system.terminate()
    }
  }

}

class HelloActor : AbstractActor() {

  data class Greeting(val msg: String, val name: String)
  data class Goodbye(val msg: String)

  override fun createReceive(): Receive {
    return receiveBuilder()
        .match(this::onGreeting)
        .match(this::onGoodbye)
        .build()
  }

  private fun onGreeting(it: Greeting) {
    println("Greetings")
    println("it.name = ${it.name}")
    println("it.msg = ${it.msg}")
  }

  private fun onGoodbye(it: Goodbye) {
    println("Goodbye")
    println("it.msg = ${it.msg}")
  }

}
