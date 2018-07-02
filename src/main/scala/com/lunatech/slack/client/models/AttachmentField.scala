package com.lunatech.slack.client.models

import play.api.libs.json.Json

case class Field(title: String, value: String, short: Boolean = false) {
  def asShort: Field = copy(short = true)
}

case class AttachmentField(
  fallback: String,
  callback_id: String,
  actions: Option[Seq[ActionField]] = None,
  text: Option[String] = None,
  title: Option[String] = None,
  id: Option[Int] = None,
  color: Option[String] = None,
  attachment_type: Option[String] = None,
  title_link: Option[String] = None,
  author_name: Option[String] = None,
  author_link: Option[String] = None,
  author_icon: Option[String] = None,
  image_url: Option[String] = None,
  thumb_url: Option[String] = None,
  footer: Option[String] = None,
  footer_icon: Option[String] = None,
  fields: Option[Seq[Field]] = None,
  pretext: Option[String] = None
) {

  def withText(text: String): AttachmentField = copy(text = Some(text))

  def withTitle(title: String): AttachmentField = copy(title = Some(title))

  def withTitleLink(titleLink: String): AttachmentField = copy(title_link = Some(titleLink))

  def withId(id: Int): AttachmentField = copy(id = Some(id))

  def withColor(color: String): AttachmentField = copy(color = Some(color))

  def addAction(actionField: ActionField): AttachmentField = copy(actions = Some(actions.getOrElse(Seq()) :+ actionField))

  def withAuthorName(authorName: String): AttachmentField = copy(author_name = Some(authorName))

  def withAuthorLink(authorLink: String): AttachmentField = copy(author_link = Some(authorLink))

  def withAuthorIcon(authorIcon: String): AttachmentField = copy(author_icon = Some(authorIcon))

  def withImageUrl(imageUrl: String): AttachmentField = copy(image_url = Some(imageUrl))

  def withThumbUrl(thumbUrl: String): AttachmentField = copy(thumb_url = Some(thumbUrl))

  def withFooterIcon(footerIcon: String): AttachmentField = copy(footer_icon = Some(footerIcon))

  def withPretext(text: String): AttachmentField = copy(pretext = Some(text))

  def withFooter(text: String): AttachmentField = copy(footer = Some(text))

  def addField(field: Field): AttachmentField = {
    fields match {
      case Some(fs) => copy(fields = Some(fs :+ field))
      case None => copy(fields = Some(List(field)))
    }
  }
}

object Field {
  implicit val fieldFormat = Json.format[Field]
}

object AttachmentField {
  implicit val attachmentFieldFormat = Json.format[AttachmentField]

  def apply(
    fallback: String,
    callback_id: String
  ): AttachmentField = new AttachmentField(fallback, callback_id)
}

