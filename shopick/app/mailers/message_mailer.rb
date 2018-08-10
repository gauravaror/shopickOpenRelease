class MessageMailer < ApplicationMailer
  default from: "hiremath@shopick.co.in"
  default to: "gauravarora.daiict@gmail.com"

  def message_email(subj, body, user)
	@body = body
	mail(to: user.email, subject: subj)
  end
end
