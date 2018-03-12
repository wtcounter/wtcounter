package wordtextcounter.details.main.util

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class RxBus {
  private val bus = PublishSubject.create<Any>()

  fun send(o: Any) {
    bus.onNext(o)
  }

  fun <T> subscribe(clazz: Class<T>, consumer: Consumer<T>): Disposable {
    return bus.filter { t -> t.javaClass == clazz }.map { t -> t as T }.subscribe(consumer)
  }

  companion object {
    val instance = RxBus()
  }
}