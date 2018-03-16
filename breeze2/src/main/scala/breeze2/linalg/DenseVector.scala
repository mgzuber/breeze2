package breeze2
package linalg

import spire.algebra._
import spire.implicits._

/**
  * A DenseVector is the "obvious" implementation of a Vector, with one twist.
  * The underlying data may have more data than the Vector, represented using an offset
  * into the array (for the 0th element), and a stride that is how far elements are apart
  * from one another.
  *
  * The i'th element is at offset + i * stride
  *
  * @author mgzuber
  *
  * @param data data array
  * @param offset index of the 0'th element
  * @param stride separation between elements
  * @param length number of elements
  */
class DenseVector[@sp(Int, Long, Double, Float) T](
  val data: Array[T],
  val offset: Int,
  val stride: Int,
  val length: Int
) {
  def this(data: Array[T]) = this(data, 0, 1, data.length)
  def this(data: Array[T], offset: Int) = this(data, offset, 1, data.length)
  def this(length: Int)(implicit man: ClassTag[T]) = this(new Array[T](length), 0, 1, length)

  def activeSize = length
  val size = length
  private[linalg] val noOffsetOrStride = offset == 0 && stride == 1

  def apply(i: Int): T = {
    if (i < -size || i >= size) throw new IndexOutOfBoundsException(i + " not in [-" + size + "," + size + ")")
    val trueI = if (i < 0) i + size else i
    if (noOffsetOrStride) {
      data(trueI)
    } else {
      data(offset + trueI * stride)
    }
  }

  def valueAt(i: Int): T = apply(i)

  def update(i: Int, update: T): Unit = {
    if (i < -size || i >= size) throw new IndexOutOfBoundsException(i + " not in [-" + size + "," + size + ")")
    val trueI = if (i < 0) i + size else i
    if (noOffsetOrStride) {
      data(trueI) = update
    } else {
      data(offset + trueI * stride) = update
    }
  }

  def copy(data: Array[T] = data, offset: Int = offset, stride: Int = stride, length: Int = length): DenseVector[T] =
    new DenseVector[T](data.clone(), offset, stride, length)

  def foreach[@sp(Unit) U](f: T => U): Unit = cfor(0)(_ < length, _ + 1){ i => val _ = f(apply(i)) }

  def *(t: T)(implicit semiGroup: Semiring[T], ct: ClassTag[T]): DenseVector[T] =
    DenseVector.tabulate[T](length)(i => apply(i) * t)
}

object DenseVector {

  /**
    * Default constructor from an array of data
    * @param data
    * @tparam T
    * @return
    */
  def apply[@sp(Int, Long, Float, Double) T](data: Array[T]): DenseVector[T] = new DenseVector[T](data)

  /**
    * Analogous to Array.tabulate
    * @param size
    * @param f
    * @tparam T
    * @return
    */
  def tabulate[@sp(Int, Long, Float, Double) T: ClassTag](size: Int)(f: Int => T): DenseVector[T] = {
    val arr = Array.tabulate(size)(f)
    apply(arr)
  }
}

trait SpireInstances {
  implicit def denseVectorEq[@sp(Int, Long, Float, Double) T: Eq]: Eq[DenseVector[T]] = new Eq[DenseVector[T]] {
    def eqv(x: DenseVector[T], y: DenseVector[T]): Boolean = x.size == y.size && {
        var b = true
        cfor(0)(_ < x.size, _ + 1) { i => b &&= x(i) === y(i) }
        b
      }
  }

  implicit def denseVectorSemiGroup[@sp(Int, Long, Float, Double) T: Semigroup: ClassTag]: Semigroup[DenseVector[T]] = new Semigroup[DenseVector[T]] {
    def combine(x: DenseVector[T], y: DenseVector[T]): DenseVector[T] = {
      assert(x.length == y.length, "Dense vectors must have same length to be combined")
      DenseVector.tabulate[T](x.length)(i => x(i) |+| y(i))
    }
  }
}
