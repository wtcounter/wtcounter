package wordtextcounter.details.main.util

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object RxBus {
  private val bus = PublishSubject.create<Event>()

  fun send(event: Event) {
    bus.onNext(event)
  }

  fun <T> subscribe(clazz: Class<T>, consumer: Consumer<T>): Disposable {
    return bus.filter { t -> t.javaClass == clazz }.map { t -> t as T }.subscribe(consumer)
  }

}