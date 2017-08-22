package org.mcdimus

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import org.mcdimus.HelloActor.Greeting
import org.mcdimus.HelloActor.SayGoodbye

class Main {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      println("Hello, World")
      val system = ActorSystem.create("MyActorSystem")
      val helloActor = system.actorOf(Props.create(HelloActor::class.java), "first-actor")
      helloActor.tell(Greeting("Hello", "Dmitri"), ActorRef.noSender())
      helloActor.tell(SayGoodbye("Bye"), ActorRef.noSender())
      
      system.terminate()
    }
  }

}

class HelloActor: AbstractActor() {

  data class Greeting(val msg: String, val name: String)
  data class SayGoodbye(val msg:String)

  override fun createReceive(): Receive {
    return receiveBuilder()
        .match(Greeting::class.java) {
          println("Greetings")
          println("it.name = ${it.name}")
          println("it.msg = ${it.msg}")
        }
        .match(SayGoodbye::class.java) {
          println("Goodbye")
          println("it.msg = ${it.msg}")
        }
        .build()
  }

}
