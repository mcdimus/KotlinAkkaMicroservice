package org.mcdimus.extensions

import akka.japi.pf.FI.UnitApply
import akka.japi.pf.ReceiveBuilder
import kotlin.reflect.KClass

/**
 * @author Dmitri Maksimov
 */
fun <T: Any> ReceiveBuilder.match(klass: KClass<T>, block: (T) -> Unit): ReceiveBuilder {
  return this.match(klass.java, block)
}

inline fun <reified T: Any> ReceiveBuilder.match(noinline block: (T) -> Unit): ReceiveBuilder {
  return this.match(T::class.java, block)
}
