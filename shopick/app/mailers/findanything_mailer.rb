class FindanythingMailer < ApplicationMailer
  default from: "hiremath@shopick.co.in"
  default to: "gauravarora.daiict@gmail.com"

  def findanything_email(find1, user)
	@find = find1
	mail(to: user.email, subject: '#FindAnything, Someone looking for you to location something!')
  end
end
