package org.mcdimus

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.pattern.Patterns
import akka.util.Timeout
import org.mcdimus.HelloActor.Goodbye
import org.mcdimus.HelloActor.Greeting
import org.mcdimus.ReturningActor.AskNameMessage
import org.mcdimus.extensions.actor
import org.mcdimus.extensions.match
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

class Main {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      println("Hello, World")
      val system = ActorSystem.create("MyActorSystem")

      try {
        // Hello Actor
        val helloActor = system.actor<HelloActor>("first-actor")
        helloActor.tell(Greeting("Hello", "Dmitri"), ActorRef.noSender())
        helloActor.tell(Goodbye("Bye"), ActorRef.noSender())

        // Returning Actor
        val returningActor = system.actor<ReturningActor>("second-actor")
        val future = Patterns.ask(returningActor, AskNameMessage("1"), Timeout(5, SECONDS))
        future.onComplete({
          println("Received at ${Instant.now()}")
          println("Answer = $it")
        }, system.dispatcher())
      } finally {
        system.terminate()
      }
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

class ReturningActor : AbstractActor() {

  data class AskNameMessage(val requestId: String)

  override fun createReceive(): Receive {
    return receiveBuilder()
        .match(this::onAskName)
        .matchAny { println("unexpected message: $it") }
        .build()
  }

  private fun onAskName(msg: AskNameMessage) {
    println("Received AskNameMessage at: ${Instant.now()}")
    TimeUnit.SECONDS.sleep(2)
    sender.tell("Dmitri", self)
  }

}
