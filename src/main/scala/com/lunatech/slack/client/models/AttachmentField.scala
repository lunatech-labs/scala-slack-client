package com.lunatech.slack.client.models

case class Field(title: String, value: String, short: Boolean = false) {
  def asShort: Field = copy(short = true)
}

case class AttachmentField(
                            fallback: String,
                            callback_id: String,
                            actions: Seq[ActionField],
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

  def addAction(actionField: ActionField): AttachmentField = copy(actions = actions :+ actionField)

  def withAuthorName(authorName: String): AttachmentField = copy(author_name = Some(authorName))

  def withAuthorLink(authorLink: String): AttachmentField = copy(author_link = Some(authorLink))

  def withAuthorIcon(authorIcon: String): AttachmentField = copy(author_icon = Some(authorIcon))

  def withImageUrl(imageUrl: String): AttachmentField = copy(image_url = Some(imageUrl))

  def withThumbUrl(thumbUrl: String): AttachmentField = copy(thumb_url = Some(thumbUrl))

  def withFooterIcon(footerIcon: String): AttachmentField = copy(footer_icon = Some(footerIcon))

  def withPretext(text: String): AttachmentField = copy(pretext = Some(text))

  def addField(field: Field): AttachmentField = {
    fields match {
      case Some(fs) => copy(fields = Some(fs :+ field))
      case None => copy(fields = Some(List(field)))
    }
  }
}

object AttachmentField {
  def apply(
             fallback: String,
             callback_id: String
           ): AttachmentField = new AttachmentField(fallback, callback_id, List())
}

case class ActionField(
                        name: String,
                        text: String,
                        `type`: String = "button",
                        id: Option[String] = None,
                        value: Option[String] = None,
                        confirm: Option[ConfirmField] = None,
                        style: Option[String] = None,
                        data_source: Option[String] = None,
                        options: Option[Seq[BasicField]] = None,
                        option_groups: Option[Seq[OptionGroupsField]] = None,
                        min_query_length: Option[Int] = None,
                        selected_options: Option[Seq[OptionField]] = None
                      ) {

  def withConfirmation(confirmField: ConfirmField): ActionField = copy(confirm = Some(confirmField))

  def withConfirmation(text: String, title: Option[String] = None, ok_text: Option[String] = None, dismiss_text: Option[String] = None): ActionField = {
    withConfirmation(ConfirmField(text, title, ok_text, dismiss_text))
  }

  def withId(id: String): ActionField = copy(id = Some(id))

  def withValue(value: String): ActionField = copy(value = Some(value))

  def asDefaultButton: ActionField = copy(style = Some("default"), `type` = "button")

  def asPrimaryButton: ActionField = copy(style = Some("primary"), `type` = "button")

  def asDangerButton: ActionField = copy(style = Some("danger"), `type` = "button")

  def asMenu(fields: Seq[BasicField], selectedOptions: Option[Seq[OptionField]] = None): ActionField = copy(`type` = "select", options = Some(fields), selected_options = selectedOptions)

  def asMenuWithGroupOption(fields: Seq[OptionGroupsField], selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", option_groups = Some(fields), selected_options = selectedOptions)
  }

  def asChannelMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("channels"), selected_options = selectedOptions)
  }

  def asUserMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("users"), selected_options = selectedOptions)
  }

  def asConversationMenu(selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("conversation"), selected_options = selectedOptions)
  }

  def asExternalMenu(minQueryLength: Option[Int] = None, selectedOptions: Option[Seq[OptionField]] = None): ActionField = {
    copy(`type` = "select", data_source = Some("external"), selected_options = selectedOptions, min_query_length = minQueryLength)
  }
}

object ActionField {
  def apply(
             name: String,
             text: String,
           ): ActionField =
    new ActionField(name, text)
}