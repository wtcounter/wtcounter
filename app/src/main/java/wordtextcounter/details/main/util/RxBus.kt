package wordtextcounter.details.main.util

import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * Created by hirak on 02/12/17.
 */
public class RxBus {
  private val bus = PublishSubject.create<Any>()

  public fun send(o: Any) {
    Log.d("send ", o.javaClass.canonicalName)
    bus.onNext(o)
  }

  public fun <T> subscribe(clazz: Class<T>, consumer: Consumer<T>): Disposable {
    Log.d("subscribe ", clazz.canonicalName)
    return bus.filter { t -> t.javaClass == clazz }.map { t -> t as T }.subscribe(consumer)
  }

  companion object {
    val instance = RxBus()
  }
}