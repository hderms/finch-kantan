package io.github.hderms

import io.github.hderms.util.FinchEncoder

object FinchKantan {
  import java.io.StringWriter
  import java.nio.charset.Charset

  import com.twitter.io.Buf
  import io.finch.{Application, Encode}
  import kantan.csv._
  import kantan.csv.ops._

  import scala.collection.TraversableOnce
  import scala.language.higherKinds
  import FinchEncoder.{csv, Csv}

  implicit def encodeCsv[A, F[_] <: TraversableOnce[_]](
      implicit e: HeaderEncoder[A]): FinchEncoder.Csv[A, F] =
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
    val string = new StringWriter

    string.writeCsv(a.asInstanceOf[TraversableOnce[A]], configuration)

    Buf.ByteBuffer.Owned(cs.encode(string.toString))
  }
}
