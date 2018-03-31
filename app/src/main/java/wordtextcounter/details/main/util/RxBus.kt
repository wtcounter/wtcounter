package wordtextcounter.details.main.util

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

object RxBus {
  private val bus = BehaviorRelay.create<Event>()

  fun send(event: Event) {
    bus.accept(event)
  }

  fun <T> subscribe(clazz: Class<T>, consumer: Consumer<T>): Disposable {
    return bus.filter { t -> t.javaClass == clazz }.map { t -> t as T }.subscribe(consumer)
  }

}