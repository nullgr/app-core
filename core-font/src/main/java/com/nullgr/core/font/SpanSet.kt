package com.nullgr.core.font

import android.text.Spannable
import java.util.*

/**
 * Class that provides ability to build and apply complicated span decor to text in easy way
 * Can be used as a method call chain (builder style) or in infix style
 *
 * Sample usage (Infix style):
 * ```
 *  val spannedText = ("Lorem ipsum dolor sit amet".applySpanSet()
 *                      add TypefaceSpan(context.getTypeface("Some.otf")) from 0 to 5
 *                      and RelativeSizeSpan(1.4f) from 7 to 9).build()
 * ```
 * Or like this (Builder style):
 * ```
 * val spannedText = SpanSet.applySpanSet("Lorem ipsum dolor sit amet")
 *                      .add(TypefaceSpan(context.getTypeface("Some.otf"))).from(0).to(5)
 *                      .and(RelativeSizeSpan(1.4f)).from(7).to(9).build()
 * ```
 * @author Nikita Grishko
 */
class SpanSet internal constructor(private var target: CharSequence?) {

    companion object {

        @JvmStatic
        fun applySpanSet(target: CharSequence?): SpanSet {
            return SpanSet(target)
        }
    }

    infix fun add(span: Any): SpanBuilderStream {
        return SpanBuilderStream().setTarget(target).setFirstSpanAndStartStream(span)
    }

    class SpanBuilderStream internal constructor() {

        private var spanSet: LinkedHashSet<SpanEntry> = linkedSetOf()
        private var target: CharSequence? = null

        private var tempSpan: Any? = null
        private var tempTextPart: String? = null
        private var tempStartIndex: Int? = null
        private var tempEndIndex: Int? = null
        private var tempFlag: Int? = null

        fun setTarget(text: CharSequence?): SpanBuilderStream {
            target = text
            return this
        }

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

        fun build(): CharSequence? {
            addTempSpanToSet()
            return applySpanSetToString()
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

        private fun applySpanSetToString(): CharSequence? {
            target?.let { nonNullTarget ->
                val spannableString = nonNullTarget.toSpannable()

                spanSet.forEach {
                    var startIndex = it.startIndex ?: 0
                    var endIndex = it.endIndex ?: nonNullTarget.length

                    if (it.textPart != null) {
                        val startIndexOfSubString = nonNullTarget.indexOf(it.textPart)
                        if (startIndexOfSubString >= 0) {
                            startIndex = startIndexOfSubString
                            endIndex = startIndex + it.textPart.length
                        }
                    }

                    spannableString.setSpan(it.span, startIndex, endIndex, it.flag
                            ?: Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                return spannableString
            }
            return null
        }
    }

    private data class SpanEntry(var startIndex: Int? = null,
                                 var endIndex: Int? = null,
                                 val textPart: String? = null,
                                 var span: Any? = null,
                                 val flag: Int? = null)
}