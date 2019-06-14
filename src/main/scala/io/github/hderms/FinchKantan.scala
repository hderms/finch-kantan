package io.github.hderms

import java.io.CharArrayWriter
import java.nio.CharBuffer

import io.github.hderms.util.FinchEncoder

object FinchKantan {
  import java.nio.charset.Charset

  import FinchEncoder.{csv, Csv}
  import com.twitter.io.Buf
  import kantan.csv._
  import kantan.csv.ops._

  import scala.collection.TraversableOnce
  import scala.language.higherKinds

  implicit def encodeCsv[A, F[_] <: TraversableOnce[_]](
      implicit e: HeaderEncoder[A]): Csv[A, F] =
    csv((a: F[A], cs: Charset) => {

      produceBufferFromCsv(a, cs, rfc.withHeader)
    })

  implicit def encodeCsvFromRow[A, F[_] <: TraversableOnce[_]](
      implicit e: RowEncoder[A]): Csv[A, F] =
    csv((a: F[A], cs: Charset) => {

      produceBufferFromCsv(a, cs, rfc.withoutHeader)
    })

  private def produceBufferFromCsv[F[_] <: TraversableOnce[_],
                                   A: HeaderEncoder](
      a: F[A],
      cs: Charset,
      configuration: CsvConfiguration): Buf = {

    val writer = new CharArrayWriter

    writer.writeCsv(a.asInstanceOf[TraversableOnce[A]], configuration)

    val buffer = CharBuffer.wrap(writer.toCharArray)

    Buf.ByteBuffer.Owned(cs.encode(buffer))
  }
}
