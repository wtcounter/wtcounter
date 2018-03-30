package wordtextcounter.details.main.store.migration


@Target(AnnotationTarget.FUNCTION)
annotation class Migration(val from: Int, val to: Int)