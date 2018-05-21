package com.nullgr.core.font

import android.text.Spannable
import java.util.*

/**
 * Class that provides ability to build complicated span decor to text in DSL way
 *
 * Sample usage:
 * ```
 *  val spannedText = (newSpanSet()
 *                      add TypefaceSpan(context.getTypeface("Some.otf")) from 0 to 5
 *                      and RelativeSizeSpan(1.4f) from 7 to 9
 *                      applyTo ("Lorem ipsum dolor sit amet"))
 * ```
 * @author Nikita Grishko
 */
class SpanSet {

    infix fun add(span: Any): SpanBuilderStream {
        return SpanBuilderStream().setFirstSpanAndStartStream(span)
    }

    class SpanBuilderStream internal constructor() {

        private var spanSet: LinkedHashSet<SpanEntry> = linkedSetOf()

        private var tempSpan: Any? = null
        private var tempTextPart: String? = null
        private var tempStartIndex: Int? = null
        private var tempEndIndex: Int? = null
        private var tempFlag: Int? = null

        fun setFirstSpanAndStartStream(span: Any): SpanBuilderStream {
            tempSpan = span
            return this
        }

        infix fun from(start: Int): SpanBuilderStream {
            this.tempStartIndex = start
            return this
        }

        infix fun to(end: Int): SpanBuilderStream {
            this.tempEndIndex = end
            return this
        }

        infix fun toText(partOfText: String): SpanBuilderStream {
            this.tempTextPart = partOfText
            return this
        }

        infix fun withFlag(flag: Int): SpanBuilderStream {
            this.tempFlag = flag
            return this
        }

        infix fun and(newSpan: Any): SpanBuilderStream {
            addTempSpanToSet()
            startNewEntryBuilding(newSpan)
            return this
        }

        infix fun applyTo(text: String): CharSequence? {
            addTempSpanToSet()
            return applySpanSetToString(text)
        }

        private fun startNewEntryBuilding(newSpan: Any) {
            tempSpan = newSpan
            tempStartIndex = null
            tempEndIndex = null
            tempFlag = null
            tempTextPart = null
        }

        private fun addTempSpanToSet() {
            tempSpan?.let {
                spanSet.add(SpanEntry(tempStartIndex, tempEndIndex, tempTextPart, tempSpan, tempFlag))
            }
        }

        private fun applySpanSetToString(text: String): CharSequence? {
            val spannableString = text.toSpannable()

            spanSet.forEach {
                var startIndex = it.startIndex ?: 0
                var endIndex = it.endIndex ?: text.length

                if (it.textPart != null) {
                    val startIndexOfSubString = text.indexOf(it.textPart)
                    if (startIndexOfSubString >= 0) {
                        startIndex = startIndexOfSubString
                        endIndex = startIndex + it.textPart.length
                    }
                }

                spannableString.setSpan(it.span, startIndex, endIndex, tempFlag
                        ?: Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            return spannableString
        }
    }

    private data class SpanEntry(var startIndex: Int? = null,
                                 var endIndex: Int? = null,
                                 val textPart: String? = null,
                                 var span: Any? = null,
                                 val flag: Int? = null)
}