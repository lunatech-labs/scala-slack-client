package models

/**
  * https://api.slack.com/dialogs
  */




case class Dialog(title: String,
                  callback_id: String,
                  elements: Seq[DialogElement],
                  submit_label: Option[String] = None,
                  notify_on_cancel: Option[Boolean] = None,
                  data_sources: Option[String] = None)

case class DialogElement(label: String,
                         name: String,
                         `type`: String,
                         max_length: Option[Int] = None,
                         min_length: Option[Int] = None,
                         optional: Option[Boolean] = None,
                         hint: Option[String] = None,
                         subtype: Option[String] = None,
                         value: Option[String] = None,
                         placeholder: Option[String] = None,
                         options: Option[DialogOption] = None,
                         option_group: Option[Seq[DialogOptionGroup]] = None)

case class DialogOption(label: String,
                        value: String)

case class DialogOptionGroup(label: String,
                             options: Seq[DialogOption]
                            )

object Dialog {
  def apply(title: String,
            callback_id: String
           ): Dialog = new Dialog(title, callback_id, List())
}