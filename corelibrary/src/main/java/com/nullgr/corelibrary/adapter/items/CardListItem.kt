package com.nullgr.corelibrary.adapter.items

abstract class CardListItem(type : CardType) : ListItem() {

  open val cardType: CardType = type

  enum class CardType {
    TOP,
    MEDIUM,
    BOTTOM
  }
}