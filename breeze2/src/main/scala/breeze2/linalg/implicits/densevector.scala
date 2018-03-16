package breeze2
package linalg
package implicits

import spire.algebra._
import spire.implicits._

object densevector extends DenseVectorSpireInstances


trait DenseVectorSpireInstances {
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
